package fr.miage.conference.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
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
}
