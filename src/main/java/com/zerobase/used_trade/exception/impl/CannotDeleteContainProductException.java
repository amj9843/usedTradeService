package com.zerobase.used_trade.exception.impl;

import com.zerobase.used_trade.data.constant.ExceptionCode;
import com.zerobase.used_trade.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CannotDeleteContainProductException extends AbstractException {
  final static ExceptionCode code = ExceptionCode.CANNOT_DELETE_CONTAIN_PRODUCT;

  @Override
  public HttpStatus getStatusCode() {
    return code.status();
  }

  @Override
  public String getMessage() {
    return code.message();
  }
}
