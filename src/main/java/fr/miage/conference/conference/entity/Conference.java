package fr.miage.conference.conference.entity;

import fr.miage.conference.session.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conference {
    @Id
    private String id = UUID.randomUUID().toString();
    private String nom;
    private String description;
    private String présentateur;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "conferenceId")
    private List<Session> sessions;
}
