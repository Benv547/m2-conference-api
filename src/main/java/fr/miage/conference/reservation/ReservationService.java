package fr.miage.conference.reservation;

import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;

import java.util.List;

public interface ReservationService {

    List<Reservation> getReservations(String conferenceId, String sessionId);

    Reservation createReservation(Reservation reservation) throws CannotProcessReservationException;
}
