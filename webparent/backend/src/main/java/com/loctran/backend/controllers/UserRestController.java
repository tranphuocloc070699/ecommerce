package com.loctran.backend.controllers;

import com.loctran.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
  @Autowired
  private UserService userService;

  @PostMapping( path="/users/check-email")
  public String checkDuplicateEmail(@Param("id") Integer id, @Param("email")String email){
    return this.userService.checkDuplicateEmail(id,email);
  }
}