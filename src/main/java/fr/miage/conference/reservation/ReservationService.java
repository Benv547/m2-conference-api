package fr.miage.conference.reservation;

import fr.miage.conference.api.dto.BankCardInformationInput;
import fr.miage.conference.bank.entity.BankCardInformation;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;

import java.util.List;

public interface ReservationService {

    List<Reservation> getReservations(String conferenceId, String sessionId);

    Reservation createReservation(Reservation reservation) throws CannotProcessReservationException;

    boolean cancelReservation(String conferenceId, String sessionId, String userId);

    boolean paymentReservation(BankCardInformation bankCardInformation, String conferenceId, String sessionId, String userId) throws CannotProcessPaymentException;

    Reservation getReservation(String conferenceId, String sessionId, String userId);
}
