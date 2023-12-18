package com.loctran.webcommon.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 128, nullable = false, unique = true)
  private String email;

  @Column(length = 64, nullable = false)
  private String password;

  @Column(name = "first_name", length = 45, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 45, nullable = false)
  private String lastName;

  @Column(length = 64)
  private String photos;

  private boolean enabled;

  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  public void addRole(Role role) {
    this.roles.add(role);
  }
}