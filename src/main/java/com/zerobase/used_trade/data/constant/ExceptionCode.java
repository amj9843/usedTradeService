package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.CodeAware;
import org.springframework.http.HttpStatus;

public enum ExceptionCode implements CodeAware {
  ALREADY_EXISTS_DOMAIN_ADDRESS(HttpStatus.CONFLICT, "이미 등록된 도메인 주소입니다."),
  NO_DOMAIN(HttpStatus.BAD_REQUEST, "식별번호에 해당하는 도메인 정보를 찾을 수 없습니다."),
  CANNOT_DELETE_DOMAIN_BEFORE_EXPIRED(HttpStatus.METHOD_NOT_ALLOWED, "만료되기 전의 도메인은 삭제할 수 없습니다."),
  ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
  NO_USER_BY_EMAIL(HttpStatus.BAD_REQUEST, "이메일 주소와 일치하는 유저 정보가 없습니다."),
  NO_USER_BY_ID(HttpStatus.NOT_FOUND, "식별번호에 해당하는 유저가 없습니다."),
  INCORRECT_PASSWORD_ON_SIGN_IN(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다."),
  INCORRECT_PASSWORD_ON_CONFIRM(HttpStatus.UNAUTHORIZED, "본인 확인을 위한 패스워드가 일치하지 않습니다."),
  EXPIRED_DOMAIN_ADDRESS(HttpStatus.UNAUTHORIZED, "만료된 도메인 주소 이용자는 이용할 수 없습니다."),
  NO_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "반드시 비밀번호 확인을 입력해야 합니다."),
  ALREADY_EXISTS_ACCOUNT(HttpStatus.CONFLICT, "이미 등록되어있는 계좌 정보입니다."),
  ALREADY_EXISTS_ADDRESS(HttpStatus.CONFLICT, "이미 등록되어있는 주소 정보입니다."),
  ALREADY_MAX_COUNT_ACCOUNT(HttpStatus.CONFLICT, "이미 등록할 수 있는 계좌 정보 개수가 모두 찼습니다."),
  ALREADY_MAX_COUNT_ADDRESS(HttpStatus.CONFLICT, "이미 등록할 수 있는 주소 정보 개수가 모두 찼습니다."),
  INVALID_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST, "등록하려는 계좌 번호가 은행과 맞지 않습니다."),
  NO_ACCOUNT(HttpStatus.NOT_FOUND, "식별번호에 해당하는 계좌 정보가 없습니다."),
  NO_AUTHORIZE(HttpStatus.FORBIDDEN, "실행 권한이 없습니다."),
  NO_ADDRESS(HttpStatus.NOT_FOUND, "식별번호에 해당하는 주소가 없습니다."),
  CANNOT_DELETE_ONLY_ONE(HttpStatus.FORBIDDEN, "대표로 설정될 다른 주소/계좌가 등록되어 있어야 합니다.");

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
