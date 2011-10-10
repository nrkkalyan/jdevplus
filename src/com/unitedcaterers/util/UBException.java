package com.unitedcaterers.util;
/**
 *A generic checked Exception used by this application for reporting any business logic exception.
 *Instead of creating multiple Exception subclasses only one subclass is used for simplicity.
 */
public class UBException extends Exception {

  public UBException() {
  }

  public UBException(String message) {
    super(message);
  }

  public UBException(String message, Throwable cause) {
    super(message, cause);
  }

  public UBException(Throwable cause) {
    super(cause);
  }
}