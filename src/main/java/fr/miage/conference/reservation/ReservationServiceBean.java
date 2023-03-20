package fr.miage.conference.reservation;

import fr.miage.conference.conference.ConferenceService;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
import fr.miage.conference.reservation.resource.ReservationResource;
import fr.miage.conference.session.SessionService;
import fr.miage.conference.session.exception.SessionNotFoundException;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.util.List;

public class ReservationServiceBean implements ReservationService {

    @Inject
    private ReservationResource resource;

    @EJB
    private ConferenceService conferenceService;

    @EJB
    private SessionService sessionService;

    @Override
    public List<Reservation> getReservations(String conferenceId, String sessionId) {
        return resource.findAllBySessionIdAndSessionConferenceId(sessionId, conferenceId);
    }

    @Override
    public Reservation createReservation(Reservation reservation) throws CannotProcessReservationException {

        try {
            var conference = conferenceService.getConference(reservation.getConferenceId());
            var session = sessionService.getSession(reservation.getConferenceId(), reservation.getSessionId());
            if (session.getNbPlacesRestantes() < reservation.getNbPlaces()) {
                throw new CannotProcessReservationException("Cannot process reservation for conference with id: " + reservation.getConferenceId()
                        + " and session with id: " + reservation.getSessionId() + " because there is not enough places");
            }

            session.setNbPlacesRestantes(session.getNbPlacesRestantes() - reservation.getNbPlaces());
            sessionService.updateSession(reservation.getConferenceId(), session);

        } catch (ConferenceNotFoundException | SessionNotFoundException e) {
            throw new CannotProcessReservationException("Cannot process reservation for conference with id: " + reservation.getConferenceId() +
                    " and session with id: " + reservation.getSessionId());
        }

        return resource.save(reservation);
    }
}
