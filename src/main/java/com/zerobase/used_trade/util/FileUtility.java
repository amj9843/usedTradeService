package com.zerobase.used_trade.util;

import java.util.Arrays;
import java.util.List;

public class FileUtility {
  public final static List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
  public final static String PRODUCT_FOLDER = "product";
  public final static String REPORT_FOLDER = "report";
  public final static String REVIEW_FOLDER = "review";

  public static boolean isImage(String imageFileName) {
    if (hasExtension(imageFileName)) {
      return IMAGE_EXTENSIONS.contains(getExtension(imageFileName));
    }

    return false;
  }

  public static boolean hasExtension(String text) {
    return findExtensionStartIndex(text) > 0;
  }

  public static String getExtension(String text) {
    if (text == null || findExtensionStartIndex(text) == 0) return "";

    return text.substring(findExtensionStartIndex(text));
  }

  public static int findExtensionStartIndex(String text) {
    return text.lastIndexOf(".") + 1;
  }
}
