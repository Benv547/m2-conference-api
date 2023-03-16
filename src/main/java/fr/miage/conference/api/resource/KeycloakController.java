package fr.miage.conference.api.resource;

import fr.miage.conference.keycloak.KeycloakService;
import fr.miage.conference.keycloak.entity.UserInput;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class KeycloakController {

    @EJB
    KeycloakService service;

    @PostMapping
    public ResponseEntity addUser(@RequestBody UserInput user){
        if (service.createUser(user)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
