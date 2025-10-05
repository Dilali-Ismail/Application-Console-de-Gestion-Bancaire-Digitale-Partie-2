package main.java.com.app.service;

import main.java.com.app.models.*;
import main.java.com.app.models.enums.FeeMode;
import main.java.com.app.models.enums.TransactionType;
import main.java.com.app.repository.impl.*;
import main.java.com.app.repository.interfaces.*;

import java.math.BigDecimal;
import java.util.Optional;

public class ExternalTransferService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private FeeRuleRepository feeRuleRepository;
    private BankFeeRepository bankFeeRepository;
    private AccountExternRepository accountExternRepository;

    public ExternalTransferService(){
        this.accountRepository = new AccountRepositoryImpl();
        this.transactionRepository = new TransactionRepositoryImpl();
        this.feeRuleRepository = new FeeRuleRepositoryImpl();
        this.bankFeeRepository = new BankFeeRepositoryImpl();
        this.accountExternRepository = new AccountExternImpl();
    }

    public void transferExtern(String accountNumberOut, String accountNumberIn, BigDecimal amount){
        // Validation des paramètres
        if (accountNumberOut == null || accountNumberOut.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte source requis");
        }
        if (accountNumberIn == null || accountNumberIn.trim().isEmpty()) {
            throw new IllegalArgumentException("Numéro de compte externe destination requis");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }

        try {
            // Recherche des comptes
            Optional<Account> FromAccount = accountRepository.findByAccountNumber(accountNumberOut);
            Optional<AccountExterne> ToAccount = accountExternRepository.findAccountExternbyId(accountNumberIn);

            if(FromAccount.isEmpty()){
                throw new IllegalArgumentException("Numéro de compte source non trouvé: " + accountNumberOut);
            }
            if(ToAccount.isEmpty()){
                throw new IllegalArgumentException("Numéro de compte externe destination non trouvé: " + accountNumberIn);
            }

            // Vérification du solde
            if(FromAccount.get().getBalance().compareTo(amount) < 0){
                throw new IllegalArgumentException("Solde insuffisant. Solde actuel: " +
                        FromAccount.get().getBalance() + ", Montant demandé: " + amount);
            }

            // Calcul des frais
            BigDecimal frais = BigDecimal.ZERO;
            Optional<FeeRule> feeRule = feeRuleRepository.findActiveByOperationType("TRANSFER_EXTERN");

            if(feeRule.isPresent()){
                frais = calculeFrais(feeRule.get(), amount);
                System.out.println("Frais calculés: " + frais + " MAD");
            } else {
                System.out.println("Aucune règle de frais active trouvée pour TRANSFER_EXTERN");
            }

            BigDecimal totalAmount = amount.add(frais);

            // Vérification solde total (montant + frais)
            if(FromAccount.get().getBalance().compareTo(totalAmount) < 0){
                throw new IllegalArgumentException("Solde insuffisant pour couvrir le montant et les frais. " +
                        "Solde: " + FromAccount.get().getBalance() + ", Total requis: " + totalAmount);
            }

            // Mise à jour des soldes
            FromAccount.get().setBalance(FromAccount.get().getBalance().subtract(totalAmount));
            // Seulement le montant principal est crédité au compte externe, pas les frais
            ToAccount.get().setBalance(ToAccount.get().getBalance().add(amount));

            // Mise à jour en base de données
            boolean accountUpdated = accountRepository.update(FromAccount.get()) != null;
            boolean externalAccountUpdated = accountExternRepository.update(ToAccount.get()) != null;

            if (!accountUpdated || !externalAccountUpdated) {
                throw new RuntimeException("Erreur lors de la mise à jour des comptes");
            }

            // Création et sauvegarde de la transaction
            Transaction transaction = new Transaction(FromAccount.get().getId(),
                    TransactionType.TRANSFER_EXTERN, // Correction du type
                    totalAmount);
            Transaction savedTransaction = transactionRepository.save(transaction);

            if (savedTransaction == null) {
                throw new RuntimeException("Erreur lors de la sauvegarde de la transaction");
            }

            // Création et sauvegarde des frais bancaires si applicable
            if(frais.compareTo(BigDecimal.ZERO) > 0){
                BankFee bankFee = new BankFee(
                        "TRANSACTION",
                        savedTransaction.getId(),
                        frais,
                        "TRANSFER_EXTERN", // Utiliser le nom de l'opération plutôt que le type de transaction
                        "MAD"
                );
                BankFee savedBankFee = bankFeeRepository.save(bankFee);
                if (savedBankFee == null) {
                    System.err.println("Attention: Les frais n'ont pas été sauvegardés");
                }
            }

            System.out.println("✅ Transfert externe réussi !");
            System.out.println("📤 Compte source: " + accountNumberOut);
            System.out.println("📥 Compte destination: " + accountNumberIn);
            System.out.println("💰 Montant transféré: " + amount + " MAD");
            System.out.println("💸 Frais appliqués: " + frais + " MAD");
            System.out.println("💳 Total débité: " + totalAmount + " MAD");
            System.out.println("🏦 Nouveau solde compte source: " + FromAccount.get().getBalance() + " MAD");

        } catch (Exception e) {
            System.err.println(" Erreur lors du transfert externe: " + e.getMessage());
            throw e; // Propager l'exception pour que le contrôleur puisse la gérer
        }
    }


    private BigDecimal calculeFrais(FeeRule feeRule, BigDecimal amount) {
        if (FeeMode.FIXED.equals(feeRule.getFeeMode())) {
            return feeRule.getFeeValue();
        }
            BigDecimal percent = feeRule.getFeeValue().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            return amount.multiply(percent);

        // return BigDecimal.ZERO;
    }
}