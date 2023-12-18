package com.loctran.backend.daos;

import com.loctran.backend.exceptions.UserNotFoundException;
import com.loctran.backend.repositories.UserRepository;
import com.loctran.webcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Repository
public class UserDataAccess {
  @Autowired
  private UserRepository userRepository;

  public List<User> findAllUsers(){
    List<User> users = new ArrayList<>();
    Iterable<User> usersIterable = userRepository.findAll();
    for (User user : usersIterable){
      users.add(user);
    }
    return users;
  }
  public User saveUser(User user){
    return userRepository.save(user);
  }
  public User isUserExistedByEmail(String email){
    return userRepository.findUserByEmail(email);
  }
  public User findById(Integer id) throws UserNotFoundException {
    try{
      return userRepository.findById(id).get();
    }catch (NoSuchElementException exception){
      throw new UserNotFoundException("Could not find any user with ID :" + id);
    }
  }
  public Integer countById(Integer id) throws UserNotFoundException {
    try{
      return userRepository.countById(id);
    }catch (NoSuchElementException exception){
      throw new UserNotFoundException("Could not find any user with ID :" + id);
    }
  }
  public void deleteById(Integer id){
    userRepository.deleteById(id);
  }
  public void updateEnabled(Integer id,Boolean enabled){
    userRepository.updateEnabled(id,enabled);
  }
}