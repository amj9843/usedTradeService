package com.zerobase.used_trade.data.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.used_trade.annotation.EntityId;
import com.zerobase.used_trade.annotation.ValidEnum;
import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.domain.Image;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ImageDto {
  @Data
  @Builder
  public static class Principle {
    private Long id;
    private Long boardId;
    private ImageUsing used;
    private String src;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Principle fromEntity(Image image) {
      return Principle.builder()
          .id(image.getId())
          .boardId(image.getBoardId())
          .used(image.getUsed())
          .src(image.getSrc())
          .createdAt(image.getCreatedAt())
          .updatedAt(image.getUpdatedAt())
          .build();
    }

    @QueryProjection
    public Principle(Long id, Long boardId, ImageUsing used, String src,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.id = id;
      this.boardId = boardId;
      this.used = used;
      this.src = src;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ImageInfo {
    @NotNull(message = "{validation.Entity.id.NotNull}")
    @EntityId
    private Long boardId;

    @NotEmpty(message = "{validation.Image.used.NotEmpty}")
    @ValidEnum(enumClass = ImageUsing.class)
    private String used;

    public static Image toEntity(Long boardId, ImageUsing used, String src) {
      return Image.builder()
          .boardId(boardId)
          .used(used)
          .src(src)
          .build();
    }
  }
}
