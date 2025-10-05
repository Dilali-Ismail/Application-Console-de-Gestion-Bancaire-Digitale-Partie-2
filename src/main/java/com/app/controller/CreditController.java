package main.java.com.app.controller;

import main.java.com.app.models.Credit;
import main.java.com.app.service.CreditService;

import java.math.BigDecimal;

public class CreditController {
   private  CreditService creditService ;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    public Credit demanderCredit(Long clientId, BigDecimal amount, int duration , BigDecimal clientIncome) {
        // Validation simple
        if (clientId == null) {
            throw new IllegalArgumentException("ID client requis");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Durée doit être positive");
        }

        return creditService.demanderCredit(clientId, amount, duration,clientIncome);
    }
}
