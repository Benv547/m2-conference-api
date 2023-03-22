package fr.miage.conference.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userId", "sessionId", "conferenceId" }) })
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
