package main.model.service;

import main.model.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User loginUser(User user);
    Optional<User> findUserByUsername(String username);
}
