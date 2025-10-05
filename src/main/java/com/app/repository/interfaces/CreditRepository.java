package main.java.com.app.repository.interfaces;

import main.java.com.app.models.Credit;

public interface CreditRepository {

      Credit save(Credit credit);
//    Optional<Credit> findById(UUID id);
//    List<Credit> findByClientId(Long clientId);
//    List<Credit> findByStatus(String status);
}
