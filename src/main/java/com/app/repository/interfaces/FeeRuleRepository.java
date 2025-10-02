package main.java.com.app.repository.interfaces;

import main.java.com.app.models.FeeRule;

import java.util.Optional;

public interface FeeRuleRepository {
    FeeRule save(FeeRule feeRule);
    Optional<FeeRule> findActiveByOperationType(String operationType);

}
