package com.loctran.backend;

import com.loctran.backend.repositories.RoleRepository;
import com.loctran.webcommon.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false) // -> when Test finished , Hibernate will roll back to don't add value to database , set false will disable rollback
public class RoleRepositoryTests {
  @Autowired
  private RoleRepository roleRepository;

  @Test
  public void testCreateFirstRole(){
    try {
      Role firstRole = Role.builder()
          .name("Admin")
          .description("manage everything")
          .build();

      Role savedRole = roleRepository.save(firstRole);
      assertThat(savedRole.getId()).isGreaterThan(0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testCreateRestRoles() {
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



    roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
  }
}