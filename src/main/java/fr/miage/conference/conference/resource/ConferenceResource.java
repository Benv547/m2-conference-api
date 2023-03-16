package fr.miage.conference.conference.resource;

import fr.miage.conference.conference.entity.Conference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceResource extends JpaRepository<Conference, String> {

    List<Conference> findAll(Specification<Conference> spec);
}
