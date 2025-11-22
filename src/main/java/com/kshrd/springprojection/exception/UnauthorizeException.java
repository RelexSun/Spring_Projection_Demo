package com.kshrd.springprojection.exception;

public class UnauthorizeException extends RuntimeException {
  public UnauthorizeException(String message) {
    super(message);
  }
}
