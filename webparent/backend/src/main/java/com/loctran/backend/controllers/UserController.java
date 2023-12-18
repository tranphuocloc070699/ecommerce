package com.loctran.backend.controllers;

import com.loctran.backend.exceptions.UserNotFoundException;
import com.loctran.backend.repositories.RoleRepository;
import com.loctran.backend.services.UserService;
import com.loctran.webcommon.entity.Role;
import com.loctran.webcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private RoleRepository roleRepository;

  @GetMapping("/users")
  public String listAllUsers(Model model){
    List<User> users = userService.listAllUsers();
    model.addAttribute("listUsers",users);
    return "users";
  }

  @GetMapping("/users/new")
  public String newUser(Model model) {
    List<Role> listRoles = userService.listAllRoles();

    User user = new User();
    user.setEnabled(true);

    model.addAttribute("user", user);
    model.addAttribute("listRoles", listRoles);
    model.addAttribute("pageTitle", "Create New User");

    return "user_form";
  }

  @PostMapping("/users/save")
  public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile file)  {
    try{
      userService.saveUser(user,file);
      redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
      return "redirect:/users";
    }catch (UserNotFoundException exception){
      redirectAttributes.addFlashAttribute("error", exception.getMessage());
      return "redirect:/users";
    }
  }

  @GetMapping("/users/edit/{id}")
  public String editUser(@PathVariable("id") Integer id,Model model,RedirectAttributes redirectAttributes){
    return userService.editUser(id,model,redirectAttributes);

  }

  @GetMapping("/users/delete/{id}")
  public String delete(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes){
    return userService.deleteUser(id,redirectAttributes);
  }

  @GetMapping("/users/{id}/update")
  public String updateEnabled(RedirectAttributes redirectAttributes,
                              @PathVariable("id")Integer id,
                              @Param("enabled")Boolean enabled){

    return userService.updateUserEnabled(redirectAttributes,id,enabled);
  }
}