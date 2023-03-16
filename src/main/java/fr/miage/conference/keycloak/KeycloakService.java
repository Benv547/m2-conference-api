package fr.miage.conference.keycloak;

import fr.miage.conference.keycloak.entity.UserInput;

public interface KeycloakService {
    boolean createUser(UserInput userInput);
}
