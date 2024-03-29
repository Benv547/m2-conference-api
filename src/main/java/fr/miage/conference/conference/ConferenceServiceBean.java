package fr.miage.conference.conference;

import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.enumeration.ConferenceMessageEnum;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;
import fr.miage.conference.conference.resource.ConferenceResource;
import fr.miage.conference.session.entity.Session;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

@Singleton
@Startup
@Service
public class ConferenceServiceBean implements ConferenceService {

    @Inject
    private ConferenceResource resource;

    @Override
    public Conference createConference(Conference conference) {
        return resource.save(conference);
    }

    @Override
    public Conference updateConference(String id, Conference conference) throws ConferenceNotFoundException {

        Conference existingConference = resource.findById(id).orElseThrow(() -> new ConferenceNotFoundException(ConferenceMessageEnum.CONFERENCE_NOT_FOUND.getMessage()));

        if (conference.getNom() != null)
            existingConference.setNom(conference.getNom());
        if (conference.getDescription() != null)
            existingConference.setDescription(conference.getDescription());
        if (conference.getPresentateur() != null)
            existingConference.setPresentateur(conference.getPresentateur());

        conference.setId(id);
        return resource.save(conference);
    }

    @Override
    public Conference getConference(String id) throws ConferenceNotFoundException {
        return resource.findById(id).orElseThrow(() -> new ConferenceNotFoundException(ConferenceMessageEnum.CONFERENCE_NOT_FOUND.getMessage()));
    }

    @Override
    public void deleteConference(String id) throws ConferenceNotFoundException {
        if (!resource.existsById(id)) {
            throw new ConferenceNotFoundException(ConferenceMessageEnum.CONFERENCE_NOT_FOUND.getMessage());
        }
        resource.deleteById(id);
    }

    @Override
    public List<Conference> getAllConferences(Specification<Conference> spec) {
        return resource.findAll(spec);
    }

    @Override
    public boolean addSession(String idConference, Session session) throws ConferenceNotFoundException {
        Conference conference = getConference(idConference);
        conference.getSessions().add(session);
        resource.save(conference);
        return true;
    }
}
