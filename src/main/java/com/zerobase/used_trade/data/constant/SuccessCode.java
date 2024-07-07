package com.zerobase.used_trade.data.constant;

import com.zerobase.used_trade.data.constant.aware.CodeAware;
import org.springframework.http.HttpStatus;

public enum SuccessCode implements CodeAware {
  CREATED_SUCCESS(HttpStatus.CREATED, "요청 정보를 성공적으로 등록했습니다."),
  READ_SUCCESS(HttpStatus.OK, "요청 정보를 성공적으로 가져왔습니다."),
  UPDATED_SUCCESS(HttpStatus.NO_CONTENT, "성공적으로 수정했습니다."),
  EXTEND_PERIOD_SUCCESS(HttpStatus.NO_CONTENT, "만료일을 연장했습니다."),
  DELETED_SUCCESS(HttpStatus.NO_CONTENT, "요청 정보가 성공적으로 삭제되었습니다.");

  private final HttpStatus status;
  private final String message;

  SuccessCode(HttpStatus status, String message) {
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
