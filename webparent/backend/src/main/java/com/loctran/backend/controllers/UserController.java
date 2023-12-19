package com.loctran.backend.controllers;

import com.loctran.backend.exceptions.UserNotFoundException;
import com.loctran.backend.repositories.RoleRepository;
import com.loctran.backend.services.UserService;
import com.loctran.backend.utils.FileUploadUtil;
import com.loctran.webcommon.entity.Role;
import com.loctran.webcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import java.io.IOException;
import java.util.List;


@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private RoleRepository roleRepository;

  @GetMapping("/users")
  public String listFirstPage(Model model){
    return listByPage(1, model, "id", "asc");
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
  public String saveUser(User user, RedirectAttributes redirectAttributes,
                         @RequestParam("image") MultipartFile multipartFile) throws IOException, UserNotFoundException {

    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      user.setPhotos(fileName);
      User savedUser = userService.saveUser(user);

      String uploadDir = "user-photos/" + savedUser.getId();

      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

    } else {
      if (user.getPhotos().isEmpty()) user.setPhotos(null);
      userService.saveUser(user);
    }


    redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

    return "redirect:/users";
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

  @GetMapping("/users/page/{pageNum}")
  public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                           @Param("sortField") String sortField, @Param("sortDir") String sortDir
  ) {
    System.out.println("Sort Field: " + sortField);
    System.out.println("Sort Order: " + sortDir);

    Page<User> page = userService.listByPage(pageNum, sortField, sortDir);

    List<User> listUsers = page.getContent();

    long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
    long endCount = startCount + UserService.USERS_PER_PAGE - 1;
    if (endCount > page.getTotalElements()) {
      endCount = page.getTotalElements();
    }

    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("currentPage", pageNum);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("startCount", startCount);
    model.addAttribute("endCount", endCount);
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("listUsers", listUsers);
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("reverseSortDir", reverseSortDir);

    return "users";
  }
}