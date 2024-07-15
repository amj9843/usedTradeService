package com.zerobase.used_trade.data.comparator;

import com.zerobase.used_trade.data.domain.Address;
import java.util.Comparator;

public class AddressComparator {
  public static class RepresentativeTopCreatedAtAsc implements Comparator<Address> {

    @Override
    public int compare(Address o1, Address o2) {
      if (!o1.isRepresentative() && o2.isRepresentative()) {
        return 1;
      } else if (o1.isRepresentative() == o2.isRepresentative()) {
        return o1.getId().compareTo(o2.getId());
      }

      return -1;
    }
  }
}
