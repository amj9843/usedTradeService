package com.zerobase.used_trade.service;

import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentRequest;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollConsignmentResponse;
import com.zerobase.used_trade.data.dto.ProductDto.EnrollDirectRequest;
import com.zerobase.used_trade.data.dto.ProductDto.Principle;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
  Principle enrollProductDirect(Long userId, List<MultipartFile> images, EnrollDirectRequest request);

  EnrollConsignmentResponse enrollProductConsignment(
      Long userId, List<MultipartFile> images, EnrollConsignmentRequest request);
}
