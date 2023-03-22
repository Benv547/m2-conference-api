package fr.miage.conference.session;

import fr.miage.conference.conference.ConferenceService;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import fr.miage.conference.session.entity.Session;
import fr.miage.conference.session.exception.CannotAddToConferenceException;
import fr.miage.conference.session.exception.SessionNotFoundException;
import fr.miage.conference.session.resource.SessionResource;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

@Singleton
@Startup
@Service
public class SessionServiceBean implements SessionService {

    @Inject
    private SessionResource resource;

    @EJB
    private ConferenceService conferenceService;

    @Override
    public Session createSession(String idConference, Session session) throws CannotAddToConferenceException {

        session.setNbPlacesRestantes(session.getNbPlaces());
        try {
            conferenceService.addSession(idConference, session);
        } catch (ConferenceNotFoundException e) {
            throw new CannotAddToConferenceException("Cannot add session to conference with id: " + idConference);
        }
        return resource.save(session);
    }

    @Override
    public List<Session> getSessionsFromConference(String idConference) {
        return resource.findAllByConferenceId(idConference);
    }

    @Override
    public Session getSession(String conferenceId, String id) throws SessionNotFoundException {
        Session session = resource.getReferenceByIdAndConferenceId(id, conferenceId);
        if (session == null) {
            throw new SessionNotFoundException("Session with id: " + id + " not found");
        }
        return session;
    }

    @Override
    public void updateSession(String conferenceId, Session session) throws SessionNotFoundException {

        Session sessionToUpdate = getSession(conferenceId, session.getId());

        if (sessionToUpdate.getNbPlacesRestantes() < session.getNbPlacesRestantes()) {
            sessionToUpdate.setNbPlacesRestantes(session.getNbPlacesRestantes());
        }

        resource.save(sessionToUpdate);
    }
}
