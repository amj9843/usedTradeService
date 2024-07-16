package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.domain.Category;
import java.util.Comparator;

public class CategoryComparator {

  public static class NameAsc implements Comparator<Category> {
    @Override
    public int compare(Category o1, Category o2) {
      if (!o1.getName().equals(o2.getName())) {
        return o1.getName().compareTo(o2.getName());
      }

      return o1.getId().compareTo(o2.getId());
    }
  }

  public static class CreatedAtDesc implements Comparator<Category> {
    @Override
    public int compare(Category o1, Category o2) {
      return o2.getId().compareTo(o1.getId());
    }
  }

  public static class CreatedAtAsc implements Comparator<Category> {
    @Override
    public int compare(Category o1, Category o2) {
      return o1.getId().compareTo(o2.getId());
    }
  }
}
