package fr.miage.conference.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Reservation {

    @Id
    private String id = UUID.randomUUID().toString();
    private String userId;
    private String sessionId;
    private String conferenceId;
    private int nbPlaces;
    private boolean payee;
    private boolean annulee;
    
}
