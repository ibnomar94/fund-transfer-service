package com.example.fundTransferService.business.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@JsonSerialize
@Setter
@Getter
public class ErrorResponse {
    String message;
    String errorCode;
    String errorDescription;
}
