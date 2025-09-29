package main.java.com.app.repository.interfaces;

import main.java.com.app.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    User save(User user);
    boolean delete(Long id);
}
