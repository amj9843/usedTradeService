package com.zerobase.used_trade.data.dto;

import com.zerobase.used_trade.annotation.EmptyOrNotBlank;
import com.zerobase.used_trade.annotation.ShortString;
import com.zerobase.used_trade.data.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CategoryDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private String name;
    private String description;

    public static Principle fromEntity(Category category) {
      return Principle.builder()
          .id(category.getId())
          .name(category.getName())
          .description(category.getDescription())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EnrollRequest {
    @NotBlank(message = "{validation.Category.name.NotBlank}")
    @ShortString
    private String name;

    @EmptyOrNotBlank
    private String description;

    public Category toEntity() {
      return Category.builder()
          .name(this.name.trim())
          .description(this.description)
          .build();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest{
    @EmptyOrNotBlank
    @ShortString
    private String name;

    @EmptyOrNotBlank
    private String description;
  }
}
