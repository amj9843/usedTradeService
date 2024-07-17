package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.constant.ImageUsing;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "image")
@Entity
public class Image extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_id")
  private Long id;

  @Column(name = "board_id")
  private Long boardId;

  @Column(name = "used")
  @Enumerated(EnumType.STRING)
  private ImageUsing used;

  @Column(name = "src")
  private String src;

  @Builder
  public Image(Long boardId, ImageUsing used, String src) {
    this.boardId = boardId;
    this.used = used;
    this.src = src;
  }
}
