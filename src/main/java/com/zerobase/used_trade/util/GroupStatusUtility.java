package com.zerobase.used_trade.util;

import static java.util.Arrays.asList;

import com.zerobase.used_trade.data.constant.DealStatus;
import com.zerobase.used_trade.data.constant.ProductStatus;
import java.util.List;

public class GroupStatusUtility {
  public final static List<DealStatus> CANNOT_READ_DEAL_DETAIL_STATUS = asList(
      DealStatus.APPLIED, DealStatus.DENIED, DealStatus.COMPLETED, DealStatus.CANCELED
  );

  public final static List<ProductStatus> CAN_APPLY_DEAL_PRODUCT_STATUS = asList(
      ProductStatus.SELLING, ProductStatus.PROCESSING
  );

  public final static List<DealStatus> CAN_APPROVE_OR_DENY_DEAL_STATUS = List.of(
      DealStatus.APPLIED
  );

  public final static List<ProductStatus> CAN_APPROVE_OR_DENY_PRODUCT_STATUS = List.of(
      ProductStatus.SELLING
  );

  public final static List<DealStatus> CAN_CANCEL_DEAL_STATUS = asList(
      DealStatus.APPLIED, DealStatus.APPROVED
  );

  public final static List<DealStatus> CAN_UPDATE_DEPOSIT_INFO_DEAL_STATUS = List.of(
      DealStatus.APPROVED
  );

  public final static List<DealStatus> CAN_CONFIRM_DEAL_STATUS = List.of(
      DealStatus.DEPOSITED
  );

  public final static List<DealStatus> CAN_REFUND_DEAL_STATUS = asList(
      DealStatus.DEPOSITED, DealStatus.CONFIRMED
  );

  public final static List<DealStatus> CAN_UPDATE_SHIPPING_INFO_DEAL_STATUS = List.of(
      DealStatus.CONFIRMED
  );

  public final static List<DealStatus> CAN_COMPLETED_MEETING_DEAL_STATUS = List.of(
      DealStatus.APPROVED
  );

  public final static List<DealStatus> CAN_COMPLETED_DEAL_STATUS = List.of(
      DealStatus.SHIPPING
  );

  public final static List<DealStatus> CANNOT_UPDATE_BUYER_INFO_DEAL_STATUS = asList(
      DealStatus.SHIPPING, DealStatus.REFUNDED, DealStatus.COMPLETED, DealStatus.CANCELED
  );

  public final static List<DealStatus> CAN_DELETE_DEAL_STATUS = asList(
      DealStatus.REFUNDED, DealStatus.CANCELED
  );
}
