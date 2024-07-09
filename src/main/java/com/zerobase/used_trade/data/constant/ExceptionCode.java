package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.CodeAware;
import org.springframework.http.HttpStatus;

public enum ExceptionCode implements CodeAware {
  ALREADY_EXISTS_DOMAIN_ADDRESS(HttpStatus.CONFLICT, "이미 등록된 도메인 주소입니다."),
  NO_DOMAIN(HttpStatus.BAD_REQUEST, "식별번호에 해당하는 도메인 정보를 찾을 수 없습니다."),
  CANNOT_DELETE_DOMAIN_BEFORE_EXPIRED(HttpStatus.METHOD_NOT_ALLOWED, "만료되기 전의 도메인은 삭제할 수 없습니다."),
  ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다.");

  private final HttpStatus status;
  private final String message;

  ExceptionCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public HttpStatus status() {
    return this.status;
  }

  @Override
  public String message() {
    return this.message;
  }
}
