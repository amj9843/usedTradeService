package com.zerobase.used_trade.data.constant.aware;

import org.springframework.http.HttpStatus;

public interface CodeAware {
  HttpStatus status();
  String message();
}
