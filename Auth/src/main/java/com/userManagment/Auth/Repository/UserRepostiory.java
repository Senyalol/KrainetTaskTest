package com.userManagment.Auth.Repository;

import com.userManagment.Auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepostiory extends JpaRepository<User,Integer> {

    User findById(int id);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
    void deleteById(int id);

    List<User> findAllByRole(String role);
}
