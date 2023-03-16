package fr.miage.conference.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionInput {

    private float prix;
    private Date date;
    private String lieu;
    private int nbPlaces;

}
