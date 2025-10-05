package main.java.com.app.models;

import main.java.com.app.models.enums.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Credit {
    private Long id;
    private BigDecimal amount;
    private int duration;
    private BigDecimal monthlyPayment;
    private CreditStatus status;
    private BigDecimal remainingAmount;
    private Long clientid;
    private LocalDate requestDate;
    private BigDecimal clientIncome;


    public Credit( BigDecimal amount, int duration, BigDecimal monthlyPayment, CreditStatus status, BigDecimal remainingAmount, Long clientId,LocalDate requestDate , BigDecimal clientIncome ) {
        this.amount = amount;
        this.duration = duration;
        this.monthlyPayment = monthlyPayment;
        this.status = status;
        this.remainingAmount = remainingAmount;
        this.clientid = clientId;
        this.requestDate = requestDate;
        this.clientIncome = clientIncome;
    }

    public Credit(Long id, BigDecimal amount, int duration, BigDecimal monthlyPayment,
                  CreditStatus status, BigDecimal remainingAmount, Long clientId,
                  LocalDate requestDate, BigDecimal clientIncome) {
        this.id = id;
        this.amount = amount;
        this.duration = duration;
        this.monthlyPayment = monthlyPayment;
        this.status = status;
        this.remainingAmount = remainingAmount;
        this.clientid = clientId;
        this.requestDate = requestDate;
        this.clientIncome = clientIncome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }


    public Long getClient() {
        return clientid;
    }

    public void setClient(Long client) {
        this.clientid = clientid;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public BigDecimal getClientIncome() {
        return clientIncome;
    }

    public void setClientIncome(BigDecimal clientIncome) {
        this.clientIncome = clientIncome;
    }
}
