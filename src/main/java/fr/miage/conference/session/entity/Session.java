package fr.miage.conference.session.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Session {

    @Id
    private String id = UUID.randomUUID().toString();
    private float prix;
    private Date date;
    private String lieu;
    @JsonIgnore
    private String conferenceId;
    private int nbPlaces;
    private int nbPlacesRestantes;

}
