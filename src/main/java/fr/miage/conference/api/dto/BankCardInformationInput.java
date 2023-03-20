package fr.miage.conference.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCardInformationInput {

    @NotNull
    @NotBlank
    @Size(min = 19, max = 19)
    private String cardNumber;
    @NotNull
    @NotBlank
    @Size(min = 7, max = 7)
    @Pattern(regexp = "[0-9]+/[0-9]+")
    private String cardExpirationDate;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String cardCvv;

}
