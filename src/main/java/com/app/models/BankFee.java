package main.java.com.app.models;
import java.math.BigDecimal;
import java.time.Instant;

public class BankFee {
    private Long id;
    private String sourceType;
    private Long sourceId;
    private BigDecimal amount;
    private String feeType;
    private String currency;
    private Instant createdAt;

    public BankFee() {
    }
    public BankFee(String sourceType, Long sourceId, BigDecimal amount, String feeType, String currency) {
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.amount = amount;
        this.feeType = feeType;
        this.currency = currency;
        this.createdAt = Instant.now();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
