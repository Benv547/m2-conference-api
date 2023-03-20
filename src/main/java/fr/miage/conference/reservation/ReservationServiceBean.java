package fr.miage.conference.reservation;

import fr.miage.conference.bank.BankService;
import fr.miage.conference.bank.entity.BankCardInformation;
import fr.miage.conference.conference.ConferenceService;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
import fr.miage.conference.reservation.resource.ReservationResource;
import fr.miage.conference.session.SessionService;
import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.exception.SessionNotFoundException;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

@Singleton
@Startup
@Service
public class ReservationServiceBean implements ReservationService {

    @Inject
    private ReservationResource resource;

    @EJB
    private ConferenceService conferenceService;

    @EJB
    private SessionService sessionService;

    @EJB
    private BankService bankService;

    @Override
    public List<Reservation> getReservations(String conferenceId, String sessionId) {
        return resource.findAllBySessionIdAndConferenceId(sessionId, conferenceId);
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

    @Override
    public boolean cancelReservation(String conferenceId, String sessionId, String userId) {

        Reservation reservation = resource.findOneBySessionIdAndConferenceIdAndUserId(sessionId, conferenceId, userId);
        if (reservation == null) {
            return false;
        }

        if (reservation.isPayee() || reservation.isAnnulee()) {
            return false;
        }

        try {
            var session = sessionService.getSession(conferenceId, sessionId);
            session.setNbPlacesRestantes(session.getNbPlacesRestantes() + reservation.getNbPlaces());
            sessionService.updateSession(conferenceId, session);
        } catch (SessionNotFoundException e) {
            return false;
        }

        reservation.setAnnulee(true);
        resource.save(reservation);
        return true;
    }

    @Override
    public boolean paymentReservation(BankCardInformation bankCardInformation, String conferenceId, String sessionId, String userId) throws CannotProcessPaymentException {

        Reservation reservation = resource.findOneBySessionIdAndConferenceIdAndUserId(sessionId, conferenceId, userId);

        if ( reservation == null || reservation.isPayee() || reservation.isAnnulee()) {
            throw new CannotProcessPaymentException("Cannot process payment for conference with id: " + conferenceId +
                    " and session with id: " + sessionId);
        }

        Session session;
        try {
            session = sessionService.getSession(conferenceId, sessionId);
        } catch (SessionNotFoundException e) {
            throw new CannotProcessPaymentException("Cannot find session with id: " + sessionId);
        }

        if (bankService.processPayment(bankCardInformation, session.getPrix())) {
            reservation.setPayee(true);
            resource.save(reservation);
            return true;
        } else {
            throw new CannotProcessPaymentException("Bank refused payment");
        }
    }
}
