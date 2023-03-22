package fr.miage.conference.bank;

import fr.miage.conference.bank.entity.BankCardInformation;

public interface BankService {

    boolean processPayment(BankCardInformation bankCardInformation, float amount);

}
