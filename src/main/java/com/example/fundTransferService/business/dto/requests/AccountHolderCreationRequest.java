package com.example.fundTransferService.business.dto.requests;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AccountHolderCreationRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String ssn;

    @NotNull
    private String mainAddress;


}
