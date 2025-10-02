package main.java.com.app.models;
import main.java.com.app.models.enums.FeeMode;
import java.math.BigDecimal;
import java.time.Instant;

public class FeeRule {
    private Long id;
    private String operationType;
    private FeeMode feeMode;
    private BigDecimal feeValue;
    private String currency;
    private Boolean isActive;
    private Instant createdAt;
    public FeeRule() {}
    public FeeRule(String operationType, FeeMode feeMode, BigDecimal feeValue, String currency, Boolean isActive) {
        this.operationType = operationType;
        this.feeMode = feeMode;
        this.feeValue = feeValue;
        this.currency = currency;
        this.isActive = isActive;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public FeeMode getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(FeeMode feeMode) {
        this.feeMode = feeMode;
    }

    public BigDecimal getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(BigDecimal feeValue) {
        this.feeValue = feeValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
