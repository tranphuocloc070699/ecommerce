package com.loctran.webcommon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 40, nullable = false, unique = true)
  private String name;

  @Column(length = 150, nullable = false)
  private String description;

  @Override
  public String toString() {
    return this.name;
  }
}