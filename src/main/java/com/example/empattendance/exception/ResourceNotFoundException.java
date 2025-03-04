package com.example.empattendance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructor for the exception.
   *
   * @param message The exception's message
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
