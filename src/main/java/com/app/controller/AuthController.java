package main.java.com.app.controller;

import main.java.com.app.models.User;
import main.java.com.app.models.enums.UserRole;
import main.java.com.app.service.AuthService;

public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username requis");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password requis");
        }

        return authService.login(username.trim(), password);
    }

    public void logout() {
        authService.logout();
    }

    public User getCurrentUser() {
        return authService.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    public void ensureTellerAccess() {
        if (!authService.isLoggedIn() || authService.getCurrentUser().getRole() != UserRole.TELLER) {
            throw new SecurityException("Accès Teller requis");
        }
    }

}
