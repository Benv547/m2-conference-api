package fr.miage.conference.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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