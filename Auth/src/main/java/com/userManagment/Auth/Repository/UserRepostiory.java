package com.userManagment.Auth.Repository;

import com.userManagment.Auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface UserRepostiory extends JpaRepository<User,Integer> {

    User findById(int id);
    User findByEmail(String email);
    User findByUsername(String username);
    void deleteById(int id);

    List<User> findAllByRole(String role);
}
