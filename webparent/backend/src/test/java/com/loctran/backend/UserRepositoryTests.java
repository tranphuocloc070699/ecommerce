package com.loctran.backend;

import com.loctran.backend.repositories.UserRepository;
import com.loctran.webcommon.entity.Role;
import com.loctran.webcommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
public class UserRepositoryTests {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager entityManager;


  @Test
  public void testCreateNewUserWithOneRole() {
    Role roleAdmin = entityManager.find(Role.class, 1);

    User userNamHM = User.builder()
        .email("nam@codejava.net")
        .password("nam2020")
        .firstName("Nam")
        .lastName("Ha Minh")
        .roles(new HashSet<>())
        .build();
    userNamHM.addRole(roleAdmin);

    User savedUser = userRepository.save(userNamHM);

    assertThat(savedUser.getId()).isGreaterThan(0);
  }

  @Test
  public void testCreateNewUserWithTwoRoles() {
    User userNamHM = User.builder()
        .email("ravi@codejava.net")
        .password("ravi2020")
        .firstName("Ravi")
        .lastName("Junior JD")
        .roles(new HashSet<>())
        .build();
    Role roleEditor = Role.builder().id(3).build();
    Role roleAssistant = Role.builder().id(5).build();

    userNamHM.addRole(roleEditor);
    userNamHM.addRole(roleAssistant);

    User savedUser = userRepository.save(userNamHM);

    assertThat(savedUser.getId()).isGreaterThan(0);
  }

  @Test
  public void testListAllUsers() {
    Iterable<User> listUsers = userRepository.findAll();
    listUsers.forEach(System.out::println);
  }

  @Test
  public void testGetUserById() {
    User userNam = userRepository.findById(1).get();
    System.out.println(userNam);
    assertThat(userNam).isNotNull();
  }

  @Test
  public void testUpdateUserDetails() {
    User userNam = userRepository.findById(1).get();
    userNam.setEnabled(true);
    userNam.setEmail("namjavaprogrammer@gmail.com");

    userRepository.save(userNam);
  }

  @Test
  public void testUpdateUserRoles() {
    User userRavi = userRepository.findById(2).get();
    Role roleEditor = Role.builder().id(3).build();
    Role roleSalesperson = Role.builder().id(2).build();

    userRavi.getRoles().remove(roleEditor);
    userRavi.addRole(roleSalesperson);

    userRepository.save(userRavi);
  }

  @Test
  public void testDeleteUser() {
    Integer userId = 2;
    userRepository.deleteById(userId);
  }

  @Test
  public void testFindUserByEmail() {
    String email = "user1@gmail.com";
    User user =  userRepository.findUserByEmail(email);
    assertThat(user).isNotNull();
  }

  @Test
  public void testCountById(){
    Integer id = 4;
    Integer counted = userRepository.countById(id);
    System.out.println(counted);
    assertThat(counted).isNotNull().isGreaterThan(0);
  }

  @Test
  @Rollback(false)
  public void testEnabled(){
    Integer id = 5;
    userRepository.updateEnabled(id,true);

  }
  @Rollback(false)
  @Test
  public void testDisabled(){
    Integer id = 5;
    userRepository.updateEnabled(id,false);
  }
}