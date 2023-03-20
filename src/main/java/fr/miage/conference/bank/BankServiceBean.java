package fr.miage.conference.bank;

import fr.miage.conference.bank.entity.BankCardInformation;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${bank.url}")
    private String serverUrl;

    @Override
    public boolean processPayment(BankCardInformation bankCardInformation, float amount) {
        try {
            String url = serverUrl + "/account/withdraw/{cartNumber}/{amount}";
            ResponseEntity<Void> response = template.postForEntity(url, null, Void.class, bankCardInformation.getCardNumber(), amount);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
