package com.loctran.backend.daos;

import com.loctran.backend.repositories.RoleRepository;
import com.loctran.webcommon.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDataAccess {

  @Autowired
  private RoleRepository roleRepository;

  public List<Role> findAllRoles(){
    List<Role> roles = new ArrayList<>();
    Iterable<Role> roleIterable = roleRepository.findAll();
    for (Role role : roleIterable){
      roles.add(role);
    }
    return roles;
  }
}