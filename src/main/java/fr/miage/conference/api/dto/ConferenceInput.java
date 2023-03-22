package fr.miage.conference.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceInput {
    @NotNull
    @NotBlank
    private String nom;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String presentateur;
}
