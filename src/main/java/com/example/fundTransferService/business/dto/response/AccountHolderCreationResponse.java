package com.example.fundTransferService.business.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonSerialize
@Setter
@Getter
public class AccountHolderCreationResponse {
    Long accountHolderId;
}
