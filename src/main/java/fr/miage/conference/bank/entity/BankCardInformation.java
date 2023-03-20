package fr.miage.conference.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCardInformation {

    private String cardNumber;
    private String cardExpirationDate;
    private String cardCvv;

}
