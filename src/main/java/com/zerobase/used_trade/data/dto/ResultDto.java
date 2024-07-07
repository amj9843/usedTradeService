package com.zerobase.used_trade.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResultDto<T, S> {
  private int code;
  private T message;
  private S data;

  public static<T, S> ResultDto<T, S> res(final HttpStatus status, final T message) {
    return res(status, message, null);
  }

  public static<T, S> ResultDto<T, S> res(final HttpStatus status, final T message, final S data) {
    return ResultDto.<T, S>builder()
        .code(status.value())
        .message(message)
        .data(data)
        .build();
  }
}
