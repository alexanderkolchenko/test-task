package com.aptech.diplom.repository.auth;

import com.aptech.diplom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    void deleteUserById(Long id);
}