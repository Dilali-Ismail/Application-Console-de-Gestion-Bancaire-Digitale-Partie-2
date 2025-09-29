package main.java.com.app.controller;

import main.java.com.app.models.Client;
import main.java.com.app.service.AuthService;
import main.java.com.app.service.ClientService;

import java.math.BigDecimal;
import java.util.List;

public class ClientController {
    private ClientService clientService;
    private AuthController authController;

    public ClientController(ClientService clientService, AuthController authController) {
        this.clientService = clientService;
        this.authController = authController;
    }


    public Client createClient(String name, String email, String phone, String address, BigDecimal monthlyIncome) {
        authController.ensureTellerAccess();

        // Validation minimale
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom requis");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email requis");
        }

        return clientService.createClient(name.trim(), email.trim(), phone, address, monthlyIncome);
    }

    public List<Client> getAllClients() {
        authController.ensureTellerAccess();
        return clientService.getAllClients();
    }
}
