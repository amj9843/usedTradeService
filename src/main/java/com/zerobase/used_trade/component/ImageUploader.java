package com.zerobase.used_trade.component;

import static com.zerobase.used_trade.util.FileUtility.getExtension;
import static com.zerobase.used_trade.util.FileUtility.isImage;
import static java.lang.String.format;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.zerobase.used_trade.exception.impl.FailedDeleteImageException;
import com.zerobase.used_trade.exception.impl.InvalidFileExtensionException;
import com.zerobase.used_trade.exception.impl.FailedUploadObjectException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUploader {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  public List<String> uploadImageList(List<MultipartFile> images, String folder) {
    List<String> successList = new ArrayList<>();
    if (images == null || images.isEmpty()) {
      return successList;
    }

    for (MultipartFile image: images) {
      try {
        successList.add(uploadImage(image, folder));
      } catch (Exception e) {
        log.error("failed to upload image. image filename -> {} ", image.getOriginalFilename());
      }
    }

    return successList;
  }

  private String uploadImage(MultipartFile image, String folder) throws IOException {
    String fileName = image.getOriginalFilename();

    if (!isImage(fileName)) {
      //이미지 파일이 아닌 경우(파일 확장자가 맞지 않는 경우)
      throw new InvalidFileExtensionException();
    }

    String uploadFileName =
        format("%s/%s_%s", folder, new Date().getTime(), UUID.randomUUID().toString().substring(0, 10));

    InputStream stream = image.getInputStream();
    byte[] bytes = IOUtils.toByteArray(stream);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(format("image/%s", getExtension(fileName)));
    metadata.setContentLength(bytes.length);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

    try{
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, uploadFileName, byteArrayInputStream, metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead);
      amazonS3.putObject(putObjectRequest);
    } catch (Exception e){
      //업로드 실패
      throw new FailedUploadObjectException();
    } finally {
      byteArrayInputStream.close();
      stream.close();
    }

    return amazonS3.getUrl(bucketName, uploadFileName).toString();
  }

  public void deleteImageByUrl(String imageAddress){
    try{
      URL url = new URL(imageAddress);
      String decodeUrl = URLDecoder.decode(url.getPath(), "UTF-8").substring(1);

      amazonS3.deleteObject(new DeleteObjectRequest(bucketName, decodeUrl));
    }catch (Exception e){
      throw new FailedDeleteImageException();
    }
  }
}
