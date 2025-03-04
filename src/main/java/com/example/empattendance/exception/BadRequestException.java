package com.example.empattendance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

  /**
   * Constructor for the exception.
   *
   * @param message The exception's message
   */
  public BadRequestException(String message) {
    super(message);
  }
}
