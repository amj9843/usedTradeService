package com.zerobase.used_trade.repository;

import com.zerobase.used_trade.data.constant.ImageUsing;
import com.zerobase.used_trade.data.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
  int countByBoardIdAndUsed(Long boardId, ImageUsing type);

  List<Image> findAllByBoardIdAndUsed(Long boardId, ImageUsing used);
}
