package main.java.com.app.service;

import main.java.com.app.models.Client;
import main.java.com.app.models.Credit;
import main.java.com.app.models.FeeRule;
import main.java.com.app.models.enums.CreditStatus;
import main.java.com.app.repository.impl.ClientRepositoryImpl;
import main.java.com.app.repository.impl.CreditRepositoryImpl;
import main.java.com.app.repository.impl.FeeRuleRepositoryImpl;
import main.java.com.app.repository.interfaces.ClientRepository;
import main.java.com.app.repository.interfaces.CreditRepository;
import main.java.com.app.repository.interfaces.FeeRuleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CreditService {

    private CreditRepository creditRepository;
    private FeeRuleRepository feeRuleRepository;
    private ClientRepository clientRepository;

    public CreditService() {
        this.creditRepository = new CreditRepositoryImpl();
        this.feeRuleRepository = new FeeRuleRepositoryImpl();
        this.clientRepository = new ClientRepositoryImpl();
    }

    private BigDecimal calculeFrais(FeeRule feeRule, BigDecimal amount) {
        if (feeRule.getFeeMode().name().equals("FIXED")) {
            return feeRule.getFeeValue();
        } else {
            BigDecimal percent = feeRule.getFeeValue().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
            return amount.multiply(percent);
        }
    }

    public Credit demanderCredit(Long clientId, BigDecimal amount, int duration ,BigDecimal clientIncome){

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

       Optional<FeeRule> Creditfrais = feeRuleRepository.findActiveByOperationType("CREDIT");
       BigDecimal frais = BigDecimal.ZERO;

      if( Creditfrais.isPresent()){
            frais = calculeFrais(Creditfrais.get() , amount );
        }

      BigDecimal totalamount = amount.add(frais);
      BigDecimal Mensuality = totalamount.divide(BigDecimal.valueOf(duration), 2 ,BigDecimal.ROUND_HALF_UP);
      BigDecimal Maxmensuality = clientIncome.multiply(new BigDecimal(0.4));

    if(Mensuality.compareTo(Maxmensuality) > 0){
        System.out.println("Mensualité trop élevée");
    }
     Credit credit = new Credit(
             amount,
             duration,
             Mensuality,
             CreditStatus.PENDING,
             totalamount,
             clientId,
             LocalDate.now(),
             clientIncome

     );
        if (credit == null) {
            throw new RuntimeException("Erreur lors de la sauvegarde du crédit");
        }

     return creditRepository.save(credit);


    }

}
