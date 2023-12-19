package com.loctran.backend.services;

import com.loctran.backend.daos.RoleDataAccess;
import com.loctran.backend.daos.UserDataAccess;
import com.loctran.backend.exceptions.UserNotFoundException;
import com.loctran.webcommon.entity.Role;
import com.loctran.webcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService {
  public static final int USERS_PER_PAGE = 5;
  @Autowired
  private UserDataAccess userDataAccess;
  @Autowired
  private RoleDataAccess roleDataAccess;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<User> listAllUsers(){
    return userDataAccess.findAllUsers();
  }

  public List<Role> listAllRoles() {return roleDataAccess.findAllRoles();}

  public User saveUser(User user) throws UserNotFoundException {
    boolean isUpdating = user.getId()!=null;
    if(isUpdating){
      User userExisting = userDataAccess.findById(user.getId());
      if(user.getPassword().isEmpty()){
        user.setPassword(userExisting.getPassword());
      }else{
        encodedPassword(user);
      }
    }else{
      encodedPassword(user);
    }

//    return null;

    return  userDataAccess.saveUser(user);
  }

  public String checkDuplicateEmail(Integer id,String email){
    try{
      boolean isCreatingNew = id ==null;
      boolean isExisted = true;
      User userExistedByEmail = userDataAccess.isUserExistedByEmail(email);
      if(isCreatingNew){
        isExisted = userExistedByEmail != null;
      }else{
        if(userExistedByEmail==null){
          isExisted = false;
        }else{
          isExisted = !Objects.equals(userExistedByEmail.getId(), id);
        }
      }
      return isExisted ? "Duplicated" : "OK";
    }catch (RuntimeException exception){
      throw new RuntimeException(exception.getMessage());
    }
  }

  private void encodedPassword(User user){
    String passwordEncoded = passwordEncoder.encode(user.getPassword());
    user.setPassword(passwordEncoded);
  }

  public String editUser(Integer id, Model model,RedirectAttributes redirectAttributes) {
    try{
      User user = userDataAccess.findById(id);
      model.addAttribute("user",user);
      model.addAttribute("listRoles", listAllRoles());
      model.addAttribute("pageTitle", "Edit User("+ id + ")");
      return "user_form";
    }catch (UserNotFoundException exception){
      redirectAttributes.addFlashAttribute("error", exception.getMessage());
      return "redirect:/users";
    }
  }

  public String deleteUser(Integer id,RedirectAttributes redirectAttributes){
    try{
      Integer counted = userDataAccess.countById(id);
      if(counted>0){
        userDataAccess.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Delete user with ID ["+id+"] successfully!");
      }else{
        throw new UserNotFoundException("User with ID ["+id+"] not found!");
      }
    }catch (UserNotFoundException exception){
      redirectAttributes.addFlashAttribute("error",exception.getMessage());
    }
    return "redirect:/users";
  }

  public String updateUserEnabled(RedirectAttributes redirectAttributes,Integer id,Boolean enabled){
    try{
      Integer counted = userDataAccess.countById(id);
      if(counted>0){
        userDataAccess.updateEnabled(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message","User with ID ["+id+"] has been "+ status +" ");
      }else{
        throw new UserNotFoundException("User with ID ["+id+"] not found!");
      }
    }catch (UserNotFoundException exception){
      redirectAttributes.addFlashAttribute("error",exception.getMessage());
    }
    return "redirect:/users";
  }


  public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
    Sort sort = Sort.by(sortField);

    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

    Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

    if (keyword != null) {
      return userDataAccess.findAll(keyword, pageable);
    }

    return userDataAccess.findAll(pageable);
  }

}