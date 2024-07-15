package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.domain.Account;
import java.util.Comparator;

public class AccountComparator {
  public static class RepresentativeTopCreatedAtAsc implements Comparator<Account> {

    @Override
    public int compare(Account o1, Account o2) {
      if (!o1.isRepresentative() && o2.isRepresentative()) {
        return 1;
      } else if (o1.isRepresentative() == o2.isRepresentative()) {
        return o1.getId().compareTo(o2.getId());
      }

      return -1;
    }
  }
}
