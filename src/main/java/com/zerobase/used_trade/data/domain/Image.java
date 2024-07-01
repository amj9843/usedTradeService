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
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_id")
  private Long id;

  @Column(name = "board_id")
  private Long boardId;

  @Column(name = "using")
  @Enumerated(EnumType.STRING)
  private ImageUsing using;

  @Column(name = "src")
  private String src;

  @Builder
  public Image(Long boardId, ImageUsing using, String src) {
    this.boardId = boardId;
    this.using = using;
    this.src = src;
  }
}
