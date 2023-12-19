package com.loctran.backend.config;

import com.loctran.backend.repositories.RoleRepository;
import com.loctran.backend.repositories.UserRepository;
import com.loctran.webcommon.entity.Role;
import com.loctran.webcommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

@Component
public class ExampleDataLoader implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void run(String... args) throws Exception {
    Optional<Role> roleExisted = roleRepository.findById(1);
    if(roleExisted.isEmpty()){
      roleDataLoader();
      userDataLoader();
      System.out.println("Example data created.");
    }
  }
  private void roleDataLoader(){
    Role roleAdmin = Role.builder()
        .name("Admin")
        .description("manage everything")
        .build();
    Role roleSalesperson = Role.builder()
        .name("Salesperson")
        .description("manage product price, "
            + "customers, shipping, orders and sales report")
        .build();

    Role roleEditor = Role.builder()
        .name("Editor")
        .description("manage categories, brands, "
            + "products, articles and menus")
        .build();
    Role roleShipper = Role.builder()
        .name("Shipper")
        .description("view products, view orders "
            + "and update order status")
        .build();
    Role roleAssistant = Role.builder()
        .name("Assistant")
        .description( "manage questions and reviews")
        .build();

    roleRepository.save(roleAdmin);
    roleRepository.save(roleSalesperson);
    roleRepository.save(roleEditor);
    roleRepository.save(roleShipper);
    roleRepository.save(roleAssistant);
  }

  private void userDataLoader(){

    for(var i =1;i<=100;i++){
      Random random = new Random();

      // Generate a random number between 1 and 5 (inclusive)
      int randomNumber = random.nextInt(5) + 1;
      User user = User.builder()
          .email("user"+i+"@gmail.com")
          .password("user"+i)
          .firstName(String.valueOf(i))
          .lastName("User")
          .roles(new HashSet<>())
          .build();
      Role role = Role.builder().id(randomNumber).build();
      user.addRole(role);
      userRepository.save(user);
    }

//    User user1 = User.builder()
//        .email("user1@gmail.com")
//        .password("user1")
//        .firstName("1")
//        .lastName("User")
//        .roles(new HashSet<>())
//        .build();
//    User user2 = User.builder()
//        .email("user2@gmail.com")
//        .password("user2")
//        .firstName("2")
//        .lastName("User")
//        .roles(new HashSet<>())
//        .build();
//    Role roleAdmin = Role.builder().id(1).build();
//    Role roleSalesPerson = Role.builder().id(2).build();
//
//    user1.addRole(roleAdmin);
//    user2.addRole(roleSalesPerson);

//    userRepository.save(user1);
//    userRepository.save(user2);
  }
}