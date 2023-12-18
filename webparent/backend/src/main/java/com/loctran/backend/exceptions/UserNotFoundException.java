package com.loctran.backend.exceptions;

public class UserNotFoundException extends Exception{
  public UserNotFoundException(String message) {
    super(message);
  }
}