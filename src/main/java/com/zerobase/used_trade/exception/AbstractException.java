package com.zerobase.used_trade.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException{
  abstract public HttpStatus getStatusCode();
  abstract public String getMessage();
}
