package io.committed.krill.extraction.exception;

/** A superclass for exceptions thrown by format extraction. */
public class ExtractionException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with a message and a cause.
   *
   * @param message the message
   * @param cause the cause
   */
  public ExtractionException(String message, Throwable cause) {
    super(message, cause);
  }
}
