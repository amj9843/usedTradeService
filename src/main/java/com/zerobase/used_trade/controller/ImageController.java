package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.ImageDto.ImageInfo;
import com.zerobase.used_trade.data.dto.ImageDto.Principle;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {
  private final ImageService imageService;

  //이미지 등록
  @Operation(summary = "이미지 등록")
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> enrollImagesOnBoard(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestPart(value = "files") List<MultipartFile> images,
      @Validated @RequestPart(value = "request") ImageInfo request) {
    List<Principle> response = this.imageService.enrollImageOnBoard(userId, images, request);

    SuccessCode code = (images.size() > response.size()) ? SuccessCode.PARTIAL_SUCCESS : SuccessCode.CREATED_SUCCESS;
    return ResponseEntity.ok(
        ResultDto.res(code.status(), code.message(), response)
    );
  }

  //이미지 삭제
  @Operation(summary = "이미지 삭제")
  @DeleteMapping
  public ResponseEntity<?> deleteImagesOnBoard(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @RequestBody Set<Long> imageIdList) {
    List<Principle> response = this.imageService.deleteImagesOnBoard(userId, imageIdList);

    SuccessCode code = (imageIdList.size() > response.size()) ?
        SuccessCode.PARTIAL_SUCCESS : SuccessCode.DELETED_SUCCESS;

    return ResponseEntity.ok(
        ResultDto.res(code.status(), code.message(), response)
    );
  }
}
