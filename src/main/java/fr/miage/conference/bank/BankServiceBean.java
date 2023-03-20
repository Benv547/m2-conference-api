package fr.miage.conference.bank;

import fr.miage.conference.bank.entity.BankCardInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@Service
public class BankServiceBean implements BankService {


    RestTemplate template = new RestTemplate();

    @Override
    public boolean processPayment(BankCardInformation bankCardInformation, float amount) {
        String url = "http://localhost:3001/account/withdraw/{cartNumber}/{amount}";
        ResponseEntity<Void> response = template.postForEntity(url, null, Void.class, bankCardInformation.getCardNumber(), amount);
        return response.getStatusCode().is2xxSuccessful();
    }
}
