package fr.miage.conference.api.controller;

import fr.miage.conference.api.dto.UserInput;
import fr.miage.conference.keycloak.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ejb.EJB;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class KeycloakController {

    @EJB
    KeycloakService service;

    @PostMapping
    public ResponseEntity addUser(@RequestBody @Valid UserInput user){
        if (service.createUser(user)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
