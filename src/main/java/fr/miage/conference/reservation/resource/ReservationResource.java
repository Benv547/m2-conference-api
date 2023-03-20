package fr.miage.conference.reservation.resource;

import fr.miage.conference.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationResource extends JpaRepository<Reservation, String> {

    List<Reservation> findAllBySessionIdAndSessionConferenceId(String sessionId, String conferenceId);
}
