package main.java.com.app.service;

import main.java.com.app.models.User;
import main.java.com.app.repository.interfaces.UserRepository;
import main.java.com.app.repository.impl.UserRepositoryImpl;

import java.util.Optional;

public class AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthService() {
        this.userRepository = new UserRepositoryImpl(); // Injection simple
    }

    // Pour les tests (injection de dépendance)
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                this.currentUser = user;
                return true;
            }
        }

        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}