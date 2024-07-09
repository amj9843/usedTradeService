package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.domain.Domain;
import java.util.Comparator;

public class DomainComparator {

  public static class NameAsc implements Comparator<Domain> {
    @Override
    public int compare(Domain o1, Domain o2) {
      if (!o1.getCompanyName().equals(o2.getCompanyName())) {
        return  o1.getCompanyName().compareTo(o2.getCompanyName());
      } else {
        return o1.getId().compareTo(o2.getId());
      }
    }
  }

  public static class CreatedAtDesc implements Comparator<Domain> {
    @Override
    public int compare(Domain o1, Domain o2) {
      return o2.getId().compareTo(o1.getId());
    }
  }

  public static class CreatedAsc implements Comparator<Domain> {
    @Override
    public int compare(Domain o1, Domain o2) {
      return o1.getId().compareTo(o2.getId());
    }
  }

  public static class EndAtAsc implements Comparator<Domain> {
    @Override
    public int compare(Domain o1, Domain o2) {
      if (o1.getEndAt().isBefore(o2.getEndAt())) {
        return 1;
      } else if (o1.getEndAt().isEqual(o2.getEndAt())) {
        if (!o1.getCompanyName().equals(o2.getCompanyName())) {
          return  o1.getCompanyName().compareTo(o2.getCompanyName());
        } else {
          return o1.getId().compareTo(o2.getId());
        }
      } else return -1;
    }
  }
}
