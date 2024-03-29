package fr.miage.conference.session;

import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.exception.CannotAddToConferenceException;
import fr.miage.conference.session.exception.SessionNotFoundException;

import java.util.List;

public interface SessionService {

    Session createSession(String idConference, Session session) throws CannotAddToConferenceException;

    List<Session> getSessionsFromConference(String idConference);

    Session getSession(String conferenceId, String id) throws SessionNotFoundException;

    void updateSession(String conferenceId, Session session) throws SessionNotFoundException;
}
