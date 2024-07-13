package com.zerobase.used_trade.util;

public class EmailUtility {
  public static String getDomainFromEmail(String email) {
    return email.substring(email.lastIndexOf("@")+1);
  }

  public static String getIdFromDomain(String email) {
    return email.substring(0, email.lastIndexOf("@"));
  }

  public static String getDomainFromDomainAddress(String domainAddress) {
    return "@".concat(domainAddress);
  }

  public static String makeEmailFromIdAndDomainAddress(String id, String domainAddress) {
    return id.concat("@").concat(domainAddress);
  }
}
