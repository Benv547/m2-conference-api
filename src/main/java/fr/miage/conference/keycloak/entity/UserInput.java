package fr.miage.conference.keycloak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInput {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}