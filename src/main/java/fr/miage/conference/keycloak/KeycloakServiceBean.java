package fr.miage.conference.keycloak;

import fr.miage.conference.api.dto.UserInput;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Singleton
@Startup
@Service
public class KeycloakServiceBean implements KeycloakService {

    private Keycloak keycloak;
    private String realm;

    @Override
    public boolean createUser(UserInput userInput) {

        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userInput.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userInput.getUserName());
        user.setFirstName(userInput.getFirstname());
        user.setLastName(userInput.getLastName());
        user.setEmail(userInput.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        try (Response response = getInstance().create(user)) {
            return response.getStatus() == 201;
        } catch (Exception e) {
            return false;
        }
    }

    public KeycloakServiceBean(@Value("${keycloak.auth-server-url}") String serverUrl,
                               @Value("${keycloak.realm}") String realm,
                               @Value("${keycloakadmin.username}") String username,
                               @Value("${keycloakadmin.password}") String password,
                               @Value("${keycloakadmin.realm}") String adminRealm,
                               @Value("${keycloakadmin.clientId}") String adminClient) {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .clientId(adminClient)
                .resteasyClient(new ResteasyClientBuilder()
                        .connectionPoolSize(10)
                        .build())
                .build();
        this.realm = realm;
    }

    private UsersResource getInstance(){
        return keycloak.realm(realm).users();
    }
}
