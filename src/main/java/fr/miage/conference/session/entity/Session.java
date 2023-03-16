package fr.miage.conference.session.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.miage.conference.conference.entity.Conference;
import fr.miage.conference.conference.resource.ConferenceResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

}
