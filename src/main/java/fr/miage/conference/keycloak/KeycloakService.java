package fr.miage.conference.keycloak;

import fr.miage.conference.api.dto.UserInput;

public interface KeycloakService {
    boolean createUser(UserInput userInput);
}
