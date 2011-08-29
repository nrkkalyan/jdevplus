package com.unitedcaterers.util;
/**
 *A generic checked Exception used by this application for reporting any business logic exception.
 *Instead of creating multiple Exception subclasses only one subclass is used for simplicity.
 */
public class UCException extends Exception {

  public UCException() {
  }

  public UCException(String message) {
    super(message);
  }

  public UCException(String message, Throwable cause) {
    super(message, cause);
  }

  public UCException(Throwable cause) {
    super(cause);
  }
}