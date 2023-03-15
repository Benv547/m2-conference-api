package fr.miage.conference.conference;

import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.exception.ConferenceNotFoundException;

import java.util.List;

public interface ConferenceService {

        Conference createConference(Conference conference);

        Conference updateConference(String id, Conference conference) throws ConferenceNotFoundException;

        Conference getConference(String id) throws ConferenceNotFoundException;

        void deleteConference(String id) throws ConferenceNotFoundException;

        List<Conference> getAllConferences();
}
