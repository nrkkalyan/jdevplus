package com.unitedcaterers.db;

public class DuplicateKeyException extends Exception {

  public DuplicateKeyException() {
  }

  public DuplicateKeyException(String message) {
    super(message);
  }

  public DuplicateKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateKeyException(Throwable cause) {
    super(cause);
  }
}