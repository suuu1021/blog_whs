package com.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password ")
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.username = :username ")
    Optional<User> findByUsername(@Param("username")String username);

}
