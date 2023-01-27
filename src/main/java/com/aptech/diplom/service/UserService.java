package com.aptech.diplom.service;

import com.aptech.diplom.exception_handler.exception.NoSuchUserException;
import com.aptech.diplom.models.User;
import com.aptech.diplom.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchUserException("There is no credit with id = " + userId));
    }
}
