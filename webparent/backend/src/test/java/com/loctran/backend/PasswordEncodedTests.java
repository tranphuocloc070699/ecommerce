package com.loctran.backend;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodedTests {
  @Test
  public void passwordEnCodedTest(){
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String rawPassword = "loctran";
    String passwordEncode = bCryptPasswordEncoder.encode(rawPassword);

    Boolean isMatched = bCryptPasswordEncoder.matches(rawPassword,passwordEncode);
  }
}