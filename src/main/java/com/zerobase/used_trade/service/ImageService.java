package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.ImageDto.ImageInfo;
import com.zerobase.used_trade.data.dto.ImageDto.Principle;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  List<Principle> enrollImageOnBoard(Long userId, List<MultipartFile> images, ImageInfo request);

  List<Principle> deleteImagesOnBoard(Long userId, Set<Long> imageIdList);
}
