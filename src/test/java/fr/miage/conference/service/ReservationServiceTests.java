package fr.miage.conference.service;

import fr.miage.conference.bank.BankService;
import fr.miage.conference.bank.entity.BankCardInformation;
import fr.miage.conference.conference.ConferenceService;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import fr.miage.conference.reservation.ReservationService;
import fr.miage.conference.reservation.entity.Reservation;
import fr.miage.conference.reservation.exception.CannotCancelReservationException;
import fr.miage.conference.reservation.exception.CannotProcessPaymentException;
import fr.miage.conference.reservation.exception.CannotProcessReservationException;
import fr.miage.conference.reservation.resource.ReservationResource;
import fr.miage.conference.session.SessionService;
import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.exception.SessionNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
@TestPropertySource(
        locations = "classpath:application-test.properties")
class ReservationServiceTests {

    @MockBean
    ReservationResource rr;

    @MockBean
    SessionService ss;

    @MockBean
    ConferenceService cs;

    @MockBean
    BankService bs;

    @Autowired
    ReservationService rs;

    @Test
    void createSession_ExpectTrue() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // ACT
        Reservation r = rs.createReservation(reservation);

        // ASSERT
        assertThat(r).isEqualTo(reservation);
    }

    @Test
    void createSession_notEnoughtPlace_ExpectException() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(0);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotProcessReservationException.class, () -> {
            rs.createReservation(reservation);
        });
    }

    @Test
    void createSession_WantReserveMorePlacesThanExists_ExpectException() throws ConferenceNotFoundException, SessionNotFoundException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(SessionNotFoundException.class);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(11);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotProcessReservationException.class, () -> {
            rs.createReservation(reservation);
        });
    }

    @Test
    void createSession_SessionNotFound_ExpectException() throws ConferenceNotFoundException, SessionNotFoundException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(SessionNotFoundException.class);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotProcessReservationException.class, () -> {
            rs.createReservation(reservation);
        });
    }

    @Test
    void createSession_ConferenceNotFound_ExpectException() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenThrow(ConferenceNotFoundException.class);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotProcessReservationException.class, () -> {
            rs.createReservation(reservation);
        });
    }

    @Test
    void createSession_ReservationAlreadyExist_ExpectException() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);

        when(rr.save(Mockito.any(Reservation.class)))
                .thenThrow(DataIntegrityViolationException.class);

        // ACT
        assertThrows(DataIntegrityViolationException.class, () -> {
            rs.createReservation(reservation);
        });
    }


    @Test
    void cancelReservation_ReservationExistAndNotPayee_ExpectTrue() throws ConferenceNotFoundException, SessionNotFoundException, CannotCancelReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(false);
        reservation.setAnnulee(false);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        // ACT
        boolean result = rs.cancelReservation("1", "1", "1");

        // ASSERT
        assertTrue(result);
    }

    @Test
    void cancelReservation_ReservationExistAndPayee_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotCancelReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(true);
        reservation.setAnnulee(false);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotCancelReservationException.class, () -> rs.cancelReservation("1", "1", "1"));
    }

    @Test
    void cancelReservation_ReservationDoesNotExist_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotCancelReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null);

        // ACT
        assertThrows(CannotCancelReservationException.class, () -> rs.cancelReservation("1", "1", "1"));
    }

    @Test
    void cancelReservation_ReservationAlreadyCancelled_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotCancelReservationException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(false);
        reservation.setAnnulee(true);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        // ACT
        assertThrows(CannotCancelReservationException.class, () -> rs.cancelReservation("1", "1", "1"));
    }


    @Test
    void paymentReservation_ReservationExistAndNotPayee_ExpectTrue() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessPaymentException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(false);
        reservation.setAnnulee(false);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        BankCardInformation bankCardInformation = new BankCardInformation();
        bankCardInformation.setCardNumber("123456789");
        bankCardInformation.setCardExpirationDate("12/12");
        bankCardInformation.setCardCvv("123");

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(true);

        // ACT
        boolean result = rs.paymentReservation(bankCardInformation, "1", "1", "1");

        // ASSERT
        assertTrue(result);
    }

    @Test
    void paymentReservation_ReservationExistAndPayee_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessPaymentException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(true);
        reservation.setAnnulee(false);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        BankCardInformation bankCardInformation = new BankCardInformation();
        bankCardInformation.setCardNumber("123456789");
        bankCardInformation.setCardExpirationDate("12/12");
        bankCardInformation.setCardCvv("123");

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(true);

        // ACT

        // ASSERT
        assertThrows(CannotProcessPaymentException.class, () -> {
            rs.paymentReservation(bankCardInformation, "1", "1", "1");
        });
    }

    @Test
    void paymentReservation_ReservationExistAndCancelled_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessPaymentException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(false);
        reservation.setAnnulee(true);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        BankCardInformation bankCardInformation = new BankCardInformation();
        bankCardInformation.setCardNumber("123456789");
        bankCardInformation.setCardExpirationDate("12/12");
        bankCardInformation.setCardCvv("123");

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(true);

        // ACT

        // ASSERT
        assertThrows(CannotProcessPaymentException.class, () -> {
            rs.paymentReservation(bankCardInformation, "1", "1", "1");
        });
    }

    @Test
    void paymentReservation_ReservationNotExist_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessPaymentException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null);

        BankCardInformation bankCardInformation = new BankCardInformation();
        bankCardInformation.setCardNumber("123456789");
        bankCardInformation.setCardExpirationDate("12/12");
        bankCardInformation.setCardCvv("123");

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(true);

        // ACT

        // ASSERT
        assertThrows(CannotProcessPaymentException.class, () -> {
            rs.paymentReservation(bankCardInformation, "1", "1", "1");
        });
    }

    @Test
    void paymentReservation_ReservationExistAndPaymentRefused_ExpectFalse() throws ConferenceNotFoundException, SessionNotFoundException, CannotProcessPaymentException {

        // ARRANGE
        List<Session> sessions = new ArrayList<>();
        Session session = new Session();
        session.setId("1");
        session.setConferenceId("1");
        session.setNbPlaces(10);
        session.setNbPlacesRestantes(10);
        session.setLieu("Test");
        sessions.add(session);

        Conference conference = new Conference();
        conference.setId("1");
        conference.setNom("Test");
        conference.setSessions(sessions);
        conference.setPresentateur("Test");

        when(cs.getConference(Mockito.anyString()))
                .thenReturn(conference);
        when(ss.getSession(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(session);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setSessionId("1");
        reservation.setConferenceId("1");
        reservation.setUserId("1");
        reservation.setNbPlaces(1);
        reservation.setPayee(false);
        reservation.setAnnulee(false);

        when(rr.findOneBySessionIdAndConferenceIdAndUserId(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(reservation);

        BankCardInformation bankCardInformation = new BankCardInformation();
        bankCardInformation.setCardNumber("123456789");
        bankCardInformation.setCardExpirationDate("12/12");
        bankCardInformation.setCardCvv("123");

        when(bs.processPayment(Mockito.any(BankCardInformation.class), Mockito.anyFloat()))
                .thenReturn(false);

        // ACT

        // ASSERT
        assertThrows(CannotProcessPaymentException.class, () -> {
            rs.paymentReservation(bankCardInformation, "1", "1", "1");
        });
    }
}
