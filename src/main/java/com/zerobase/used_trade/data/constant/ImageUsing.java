package com.zerobase.used_trade.data.constant;

import static com.zerobase.used_trade.util.CountUtility.PRODUCT_MAX_IMG;
import static com.zerobase.used_trade.util.CountUtility.REPORT_MAX_IMG;
import static com.zerobase.used_trade.util.CountUtility.REVIEW_MAX_IMG;
import static com.zerobase.used_trade.util.FileUtility.PRODUCT_FOLDER;
import static com.zerobase.used_trade.util.FileUtility.REPORT_FOLDER;
import static com.zerobase.used_trade.util.FileUtility.REVIEW_FOLDER;
import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;

public enum ImageUsing implements DescriptionAware {
  PRODUCT("상품 이미지", PRODUCT_FOLDER, PRODUCT_MAX_IMG),
  REPORT("신고/건의 이미지", REPORT_FOLDER, REPORT_MAX_IMG),
  REVIEW("후기 이미지", REVIEW_FOLDER, REVIEW_MAX_IMG);

  private final String description;
  private final String folder;
  private final int maxCount;

  ImageUsing(String description, String folder, int maxCount) {
    this.description = description;
    this.folder = folder;
    this.maxCount = maxCount;
  }

  public String folder() {
    return this.folder;
  }

  public int maxCount() {
    return this.maxCount;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String toString() {
    return format("%s(%s)", this.name(), this.description);
  }
}
