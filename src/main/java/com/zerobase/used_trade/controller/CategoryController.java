package com.zerobase.used_trade.controller;

import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.CategorySortType;
import com.zerobase.used_trade.data.constant.DomainSortType;
import com.zerobase.used_trade.data.constant.SuccessCode;
import com.zerobase.used_trade.data.dto.CategoryDto.EnrollRequest;
import com.zerobase.used_trade.data.dto.CategoryDto.UpdateRequest;
import com.zerobase.used_trade.data.dto.ResultDto;
import com.zerobase.used_trade.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
  private final CategoryService categoryService;

  //카테고리 등록
  @Operation(summary = "카테고리 등록")
  @PostMapping
  public ResponseEntity<?> enrollCategory(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @Validated @RequestBody EnrollRequest request) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message(),
            this.categoryService.enrollCategory(userId, request))
    );
  }

  //카테고리 전체 조회
  @Operation(summary = "카테고리 전체 조회")
  @GetMapping
  public ResponseEntity<?> getCategoryList(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "criteria", required = false, defaultValue = "NAMEASC")
      @ValidEnum(enumClass = DomainSortType.class) String criteria) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(),
            this.categoryService.getCategoryList(page, size, CategorySortType.valueOf(criteria))));
  }

  //카테고리 부분 조회
  @Operation(summary = "카테고리 부분 조회")
  @GetMapping("/{categoryId}")
  public ResponseEntity<?> getCategory(@PathVariable("categoryId") Long categoryId) {
    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.READ_SUCCESS.status(),
            SuccessCode.READ_SUCCESS.message(), this.categoryService.getCategory(categoryId)));
  }

  //카테고리 수정
  @Operation(summary = "카테고리 수정")
  @PatchMapping("/{categoryId}")
  public ResponseEntity<?> updateCategory(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("categoryId") Long categoryId,
      @Validated @RequestBody UpdateRequest request
  ) {
    this.categoryService.updateCategoryInfo(userId, categoryId, request);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.UPDATED_SUCCESS.status(), SuccessCode.UPDATED_SUCCESS.message())
    );
  }

  //카테고리 삭제
  @Operation(summary = "카테고리 삭제")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<?> deleteCategory(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("categoryId") Long categoryId
  ) {
    this.categoryService.deleteCategory(userId, categoryId);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
  
  @Operation(summary = "카테고리별 상품 등록")
  @PostMapping("/match-products/{categoryId}")
  public ResponseEntity<?> enrollProductsOnCategory(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("categoryId") Long categoryId,
      @RequestBody Set<Long> productIdList
  ) {
    this.categoryService.enrollProductsOnCategory(userId, categoryId, productIdList);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.CREATED_SUCCESS.status(), SuccessCode.CREATED_SUCCESS.message())
    );
  }

  @Operation(summary = "카테고리별 상품 삭제")
  @DeleteMapping("/match-products/{categoryId}")
  public ResponseEntity<?> deleteProductsOnCategory(
      //TODO JWT 사용이후는 @AuthenticationPrincipal 이용, CustomUserDetails 가져옴
      @RequestHeader("Authorization") Long userId,
      @PathVariable("categoryId") Long categoryId,
      @RequestBody Set<Long> productIdList
  ) {
    this.categoryService.deleteProductsOnCategory(userId, categoryId, productIdList);

    return ResponseEntity.ok(
        ResultDto.res(SuccessCode.DELETED_SUCCESS.status(), SuccessCode.DELETED_SUCCESS.message())
    );
  }
}
