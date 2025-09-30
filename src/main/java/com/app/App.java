package main.java.com.app;

import main.java.com.app.config.DatabaseConnection;
import main.java.com.app.controller.AccountController;
import main.java.com.app.controller.TransactionController;
import main.java.com.app.service.AccountService;
import main.java.com.app.service.AuthService;
import main.java.com.app.service.ClientService;
import main.java.com.app.controller.AuthController;
import main.java.com.app.controller.ClientController;
import main.java.com.app.service.TransactionService;
import main.java.com.app.view.MenuTeller;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        AuthService authService = new AuthService();
        ClientService clientService = new ClientService();
        AuthController authController = new AuthController(authService);
        ClientController clientController = new ClientController(clientService, authController);
        AccountService accountService =  new AccountService();
        AccountController accountController = new AccountController(accountService,authController);
        TransactionService transactionService = new TransactionService();
        TransactionController transactionController = new TransactionController(transactionService);

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\n=== BANK APP ===");
                System.out.println("1. Login");
                System.out.println("2. Quitter");
                System.out.print("Choix : ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        login(authController, clientController, scanner ,accountController,transactionController);
                        break;
                    case "2":
                        System.out.println("Au revoir !");
                        dbConnection.closeConnection();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Choix invalide");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur application : " + e.getMessage());
        }
    }

    private static void login(AuthController authController, ClientController clientController, Scanner scanner , AccountController accountcontroller,TransactionController transactionController) {
        try {
            System.out.print("Username : ");
            String username = scanner.nextLine();

            System.out.print("Password : ");
            String password = scanner.nextLine();

            if (authController.login(username, password)) {
                System.out.println("Login réussi !");

                switch (authController.getCurrentUser().getRole()) {
                    case TELLER:
                        new MenuTeller(authController, clientController,accountcontroller,transactionController).displayMenu();
                        break;
                    default:
                        System.out.println("Menu pour " + authController.getCurrentUser().getRole() + " bientôt disponible");
                        authController.logout();
                }
            } else {
                System.out.println("Login échoué");
            }
        } catch (Exception e) {
            System.out.println("Erreur login : " + e.getMessage());
        }
    }
}