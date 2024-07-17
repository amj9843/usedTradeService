package com.zerobase.used_trade.exception.impl;

import com.zerobase.used_trade.data.constant.ExceptionCode;
import com.zerobase.used_trade.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CannotUploadImagesOverTheMaxCountException extends AbstractException {
  final static ExceptionCode code = ExceptionCode.CANNOT_UPLOAD_IMAGE_OVER_THE_MAX_COUNT;

  @Override
  public HttpStatus getStatusCode() {
    return code.status();
  }

  @Override
  public String getMessage() {
    return code.message();
  }
}
