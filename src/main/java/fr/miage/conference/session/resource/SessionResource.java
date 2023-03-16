package fr.miage.conference.session.resource;

import fr.miage.conference.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionResource extends JpaRepository<Session, String> {
    List<Session> findAllByConferenceId(String idConference);

    Session getReferenceByIdAndConferenceId(String id, String idConference);
}
