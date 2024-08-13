package com.example.fundTransferService.business.dto.response;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonSerialize
@Setter
@Getter
public class AccountHolderDetailsResponse {

    private Long accountHolderId;

    private String firstName;

    private String lastName;

    private String ssn;

    private Map<String,String> currentAccounts;
}
