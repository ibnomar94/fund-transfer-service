package com.example.fundTransferService.business.dto.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreationRequest {

    @NotBlank
    private String currency;
}
