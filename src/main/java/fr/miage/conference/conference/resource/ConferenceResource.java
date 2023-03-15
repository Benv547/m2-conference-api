package fr.miage.conference.conference.resource;

import fr.miage.conference.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceResource extends JpaRepository<Conference, String> {

}
