package main.java.com.app.view;

import main.java.com.app.controller.*;
import main.java.com.app.models.Account;
import main.java.com.app.models.Client;
import main.java.com.app.models.Credit;
import main.java.com.app.models.enums.AccountType;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.List;
import java.util.Scanner;

public class MenuTeller {
    private AuthController authController;
    private ClientController clientController;
    private AccountController accountController;
    private TransactionController transactionController;
    public  CreditController CreditController ;
    private Scanner scanner;

    public MenuTeller(AuthController authController, ClientController clientController , AccountController accountController, TransactionController transactionController , CreditController CreditController ) {
        this.authController = authController;
        this.clientController = clientController;
        this.accountController = accountController;
        this.transactionController = transactionController;
        this.CreditController = CreditController;
        this.scanner = new Scanner(System.in);

    }

    public void displayMenu() {
        while (authController.isLoggedIn()) {
            System.out.println("\n=== MENU TELLER ===");
            System.out.println("Connecté : " + authController.getCurrentUser().getUsername());
            System.out.println("1. Créer client");
            System.out.println("2. Lister clients");
            System.out.println("=== GESTION DES COMPTES ===");
            System.out.println("3 - Creer un compte");
            System.out.println("4 - Lister les aacounts d'un Client");
            System.out.println("===== transaction ====");
            System.out.println("5. Dépôt");
            System.out.println("6. Withdraw");
            System.out.println("7. transfer");
            System.out.println("8. transfer extern");
            System.out.println("===== CREDITS =====");
            System.out.println("9. demander un credit ");
            System.out.println("10. Déconnexion");
            System.out.print("Choix : ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": createClient(); break;
                case "2": listClients(); break;
                case "3" : creerAccount();break;
                case "4": ListeclientAccounts(); break;
                case "5": deposit(); break;
                case "6": withdraw(); break;
                case "7": transfer(); break;
                case "8" : transferExtern();break;
                case "9" : demanderCredit(); break;
                case "10": logout(); return;
                default: System.out.println("Choix invalide");
            }
        }
    }

    private void createClient() {
        System.out.println("\n--- Création client ---");

        try {
            System.out.print("Nom complet : ");
            String name = scanner.nextLine();

            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Téléphone : ");
            String phone = scanner.nextLine();

            System.out.print("Adresse : ");
            String address = scanner.nextLine();

            System.out.print("Revenu mensuel : ");
            BigDecimal monthlyIncome = new BigDecimal(scanner.nextLine());

            Client client = clientController.createClient(name, email, phone, address, monthlyIncome);
            System.out.println(" Client créé ! ID : " + client.getId());

        } catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void listClients() {
        System.out.println("\n--- Liste clients ---");

        try {
            var clients = clientController.getAllClients();

            if (clients.isEmpty()) {
                System.out.println("Aucun client");
            } else {
                for (Client client : clients) {
                    System.out.printf("ID: %d | Nom: %s | Email: %s | Revenu: %.2f€%n",
                            client.getId(), client.getName(), client.getEmail(), client.getMonthlyIncome());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }

    private void logout() {
        authController.logout();
        System.out.println("Déconnecté");
    }

    private void creerAccount(){
        System.out.println("*** CRÉATION D'UN COMPTE BANCAIRE ***");
        try{
            System.out.println("Id de client");
            Long clientId = Long.parseLong(scanner.nextLine());
            System.out.println("Account Type");

            Client client = new Client();
            client.setId(clientId);

            System.out.println("\nTypes de compte disponibles :");
            System.out.println("1. Compte Courant (CHECKING)");
            System.out.println("2. Compte Épargne (SAVINGS)");
            System.out.println("3. Compte Crédit (CREDIT)");
            System.out.print("Votre choix : ");

            int ChoixdeType = scanner.nextInt();
            AccountType type = null ;
            switch (ChoixdeType) {
                case 1:
                    type = AccountType.CHECKING;
                    break;
                case 2:
                    type = AccountType.SAVINGS;
                    break;
                case 3:
                    type = AccountType.CREDIT;
                    break;
                default:
                    System.out.println("Choix invalide. Opération annulée.");
                    return;
            }
            scanner.nextLine();
            System.out.print("Solde initial : ");
            BigDecimal initialBalance = new BigDecimal(scanner.nextLine());
            Account account  = accountController.creerAccount(client ,type , initialBalance );
            System.out.println("\n Compte créé avec succès !");
            System.out.println("Numéro de compte : " + account.getAccountNumber());
            System.out.println("Type : " + account.getAccountType());
            System.out.println("Solde initial : " + account.getBalance() + " " + account.getCurrency());
            System.out.println("Client ID : " + account.getClientId());
        }catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void ListeclientAccounts(){
        System.out.println("**COMPTES D'UN CLIENT***");
        try {
            System.out.println("Id de client :");
            Long clientId = Long.parseLong(scanner.nextLine());
            List<Account> accounts = accountController.getClientsAccounts(clientId);
            if (accounts.isEmpty()) {
                System.out.println("Aucun compte pour ce client");
            }
            System.out.println("\n Comptes du client " + clientId + " :");
            System.out.println("-".repeat(80));
            System.out.printf("%-15s %-12s %-10s %-12s %-10s%n",
                    "NUMÉRO", "TYPE", "SOLDE", "DEVISE", "STATUT");
            System.out.println("-".repeat(80));
            for (Account account : accounts) {
                System.out.printf("%-15s %-12s %-10.2f %-12s %-10s%n",
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getCurrency(),
                        account.getStatus());
            }
            System.out.println("-".repeat(80));
        }catch(Exception e){
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void deposit(){
            System.out.println("--- DEPOT ---");
            try {
                System.out.print("Numero de compte : ");
                String accountNumber = scanner.nextLine();
                System.out.print("Montant a déposer : ");
                BigDecimal amount = new BigDecimal(scanner.nextLine());
                transactionController.deposit(accountNumber, amount);
                BigDecimal newBalance = transactionController.getAccountBalance(accountNumber);
                System.out.println(" Dépôt réussi !");
                System.out.println("Nouveau solde : " + newBalance + " MAD");

            }  catch (Exception e) {
                System.out.println(" Erreur : " + e.getMessage());
            }
        }

    private void withdraw(){
        System.out.println("--- withdraw ---");
        try {
            System.out.print("Numero de compte : ");
            String accountNumber = scanner.nextLine();
            System.out.print("Montant a Withdrawing : ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            transactionController.withdraw(accountNumber, amount);
            BigDecimal newBalance = transactionController.getAccountBalance(accountNumber);
            System.out.println(" Withdraw réussi !");
            System.out.println("Nouveau solde : " + newBalance + " MAD");

        }  catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void transfer(){
        System.out.println("--- TRANSFER ---");
        try{
            System.out.print("Numero de compte Out : ");
            String accountNumberOut = scanner.nextLine();
            System.out.print("Numero de compte In : ");
            String accountNumberIn = scanner.nextLine();
            System.out.print("Montant a Transfer : ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            transactionController.transfer(accountNumberOut,accountNumberIn,amount);
            System.out.println(" Transfer réussi !");
        }catch (Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void transferExtern(){
        System.out.println("--- TRANSFER Extern ---");
        try {
            System.out.print("Numero de compte Out : ");
            String accountNumberOut = scanner.nextLine();
            System.out.print("Numero de compte extern In : ");
            String accountNumberIn = scanner.nextLine();
            System.out.print("Montant a Transfer : ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            transactionController.transferExtern(accountNumberOut, accountNumberIn, amount);
            System.out.println(" Transfer réussi !");
        }catch(Exception e) {
            System.out.println(" Erreur : " + e.getMessage());
        }
    }

    private void demanderCredit() {
        System.out.println("--- DEMANDE DE CRÉDIT ---");
        try {
            System.out.print("ID du client : ");
            Long clientId = Long.parseLong(scanner.nextLine());

            System.out.print("Montant du crédit : ");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            System.out.print("Durée  : ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print(" Salaire : ");
            BigDecimal Salaire = new BigDecimal(scanner.nextLine());

           Credit credit = CreditController.demanderCredit(clientId, amount, duration,Salaire);

            System.out.println("✅ Demande de crédit créée avec succès !");
            System.out.println("📋 Détails du crédit:");
            System.out.println("   - ID: " + credit.getId());
            System.out.println("   - Montant: " + credit.getAmount() + " MAD");
            System.out.println("   - Durée: " + credit.getDuration() + " mois");
            System.out.println("   - Mensualité: " + credit.getMonthlyPayment() + " MAD");
            System.out.println("   - Statut: " + credit.getStatus());
            System.out.println("⏳ En attente de validation par le manager...");

        } catch (Exception e) {
            System.out.println(" Erreur: " + e.getMessage());
        }
    }
}


