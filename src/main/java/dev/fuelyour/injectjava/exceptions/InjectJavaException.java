package dev.fuelyour.injectjava.exceptions;

abstract class InjectJavaException extends RuntimeException {

  InjectJavaException() {
    super();
  }

  InjectJavaException(String message) {
    super(message);
  }

  InjectJavaException(String message, Throwable cause) {
    super(message, cause);
  }

  InjectJavaException(Throwable cause) {
    super(cause);
  }

  InjectJavaException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
