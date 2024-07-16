package com.zerobase.used_trade.data.domain;

import com.zerobase.used_trade.data.dto.CategoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "category")
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  public void update(CategoryDto.UpdateRequest request) {
    if (request.getName() != null && !request.getName().isBlank()) {
      this.name = request.getName().trim();
    }
    if (request.getDescription() != null) {
      this.description = request.getDescription();
    }
  }

  @Builder
  public Category(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
