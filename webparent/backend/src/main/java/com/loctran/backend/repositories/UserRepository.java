package com.loctran.backend.repositories;

import com.loctran.webcommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
public interface UserRepository extends JpaRepository<User,Integer> {

  @Query("SELECT u FROM User u WHERE u.email = :email")
  User findUserByEmail(@Param("email")String email);

  @Query("SELECT COUNT(u) FROM User u WHERE u.id = :id")
  Integer countById(Integer id);

  @Modifying
  @Query(value = "UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
  void updateEnabled(Integer id, Boolean enabled);


  @Override
  public Page<User> findAll(String keyword, Pageable pageable);


}