package com.zerobase.used_trade.data.constant;

import static java.lang.String.format;

import com.zerobase.used_trade.data.constant.aware.DescriptionAware;
import java.util.Set;

public enum Bank implements DescriptionAware {
  //금융결제원_CMS_계좌번호_체계(24.06.24): https://www.cmsedi.or.kr/cms/board/workdata/view/991
  SANUP("산업은행", Set.of(11, 14)),
  IBK("기업은행", Set.of(10, 11, 12, 14)),
  KB("국민은행", Set.of(10, 11, 12, 14)),
  HANA("하나은행", Set.of(11, 12, 14)),
  SUHUP("수협은행/수협중앙회", Set.of(11, 12, 14)),
  NHBANK("농협은행", Set.of(11, 12, 13)),
  NHCENTRAL("농협중앙회", Set.of(13, 14)),
  WOORI("우리은행", Set.of(11, 12, 13, 14)),
  SC("SC제일은행/제일은행", Set.of(11, 14)),
  HKCITY("한국씨티은행", Set.of(10, 11, 12, 13)),
  IAM("아이엠은행/대구은행", Set.of(7, 8, 9, 10, 11, 12, 13, 14)),
  BUSAN("부산은행", Set.of(12, 13)),
  GWANGJU("광주은행", Set.of(12, 13)),
  JEJU("제주은행", Set.of(10, 12)),
  JEONBUK("전북은행", Set.of(12, 13)),
  GYEONGNAM("경남은행", Set.of(12, 13)),
  SAEMAEUL("새마을금고", Set.of(13)),
  SINHYUP("신협중앙회", Set.of(10, 11, 12, 13, 14)),
  SANGHO("상호저축은행", Set.of(14)),
  HSBC("HSBC은행", Set.of(12)),
  DOICHI("도이치은행", Set.of(10)),
  JPMOGANCHASE("제이피모간체이스은행", Set.of(10)),
  BOA("BOA은행", Set.of(12, 14)),
  BNPPARIVA("비엔피파리바은행", Set.of(14)),
  SANRIM("산립조합중앙회", Set.of(12, 13)),
  POSTOFFICE("지식경제부우체국", Set.of(12, 13, 14)),
  SINHAN("신한은행", Set.of(11, 12, 13, 14)),
  KBANK("케이뱅크", Set.of(10, 12, 13, 14)),
  KAKAO("카카오뱅크", Set.of(13)),
  TOSS("토스뱅크", Set.of(12));

  private final String description;
  private final Set<Integer> accountNumberLengths;

  Bank(String description, Set<Integer> accountNumberLengths) {
    this.description = description;
    this.accountNumberLengths = accountNumberLengths;
  }

  public Boolean isMatch(String text) {
    return accountNumberLengths.stream().anyMatch(it -> it == text.length());
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
