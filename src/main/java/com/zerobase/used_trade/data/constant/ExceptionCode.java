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
  CANNOT_DELETE_ONLY_ONE(HttpStatus.FORBIDDEN, "대표로 설정될 다른 주소/계좌가 등록되어 있어야 합니다."),
  ALREADY_EXISTS_CATEGORY(HttpStatus.CONFLICT, "이미 중복되는 카테고리명이 있습니다."),
  NO_CATEGORY(HttpStatus.NOT_FOUND, "식별번호에 해당하는 카테고리가 없습니다."),
  CANNOT_DELETE_CONTAIN_PRODUCT(HttpStatus.FORBIDDEN, "카테고리에 속하는 상품이 있으면 카테고리 삭제가 불가합니다."),
  FAILED_DELETE_IMAGE(HttpStatus.CONFLICT, "이미지 삭제에 실패했습니다."),
  FAILED_UPLOAD_OBJECT(HttpStatus.CONFLICT, "파일 업로드에 실패했습니다."),
  INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "업로드하려는 파일 형식이 잘못되었습니다."),
  CANNOT_UPLOAD_IMAGE_OVER_THE_MAX_COUNT(HttpStatus.BAD_REQUEST, "업로드하려는 이미지의 최대 개수를 넘어섰습니다."),
  CANNOT_USE_BEFORE_ENROLL_ACCOUNT(HttpStatus.CONFLICT, "계좌 정보를 등록하지 않고 이용할 수 없습니다."),
  CANNOT_USE_BEFORE_ENROLL_ADDRESS(HttpStatus.CONFLICT, "주소 정보를 등록하지 않고 이용할 수 없습니다."),
  NO_MATCH_ACCOUNT_AND_USER(HttpStatus.CONFLICT, "사용자가 등록한 계좌 정보 중 입력한 계좌 식별번호와 일치하는 것이 없습니다."),
  NO_MATCH_ADDRESS_AND_USER(HttpStatus.CONFLICT, "사용자가 등록한 주소 정보 중 입력한 주소 식별번호와 일치하는 것이 없습니다."),
  CANNOT_UPLOAD_ONLY_IMAGES_OF_REPORT(HttpStatus.FORBIDDEN, "건의/신고는 등록 후 수정할 수 없습니다."),
  ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 완료된 항목엔 요청을 처리할 수 없습니다."),
  NO_REPORT(HttpStatus.NOT_FOUND, "식별번호에 해당하는 신고/건의사항이 없습니다."),
  CANNOT_REPORT_SELF_EXCEPTION(HttpStatus.BAD_REQUEST, "자기 자신은 신고할 수 없습니다."),
  CANNOT_CHANGE_ROLE(HttpStatus.CONFLICT, "권한을 바꿀 수 있는 대상이 아닙니다."),
  NO_PRODUCT(HttpStatus.CONFLICT, "식별번호에 해당하는 상품이 없습니다."),
  NO_DEAL_METHOD(HttpStatus.CONFLICT, "식별번호에 해당하는 거래 방식이 없습니다."),
  CANNOT_DELETE_DEAL_METHOD(HttpStatus.FORBIDDEN, "해당 거래 방식으로 신청중이거나 진행중인 거래가 있으면 거래 방식 삭제가 불가합니다.");

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
