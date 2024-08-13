package com.example.fundTransferService.business.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.fundTransferService.business.dto.requests.AccountHolderCreationRequest;
import com.example.fundTransferService.business.dto.response.AccountHolderCreationResponse;
import com.example.fundTransferService.business.dto.response.AccountHolderDetailsResponse;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;

@Mapper(componentModel = "spring")
public interface AccountHolderMapper {
    AccountHolder fromAccountHolderCreationRequest(AccountHolderCreationRequest accountHolderCreationRequest);

    @Mapping(target = "accountHolderId", source = "id")
    @Mapping(target = "currentAccounts", source = "accountHolder.accounts", qualifiedByName = "mapAccountsDetails")
    AccountHolderDetailsResponse accountHolderToAccountHolderDetailsResponse(AccountHolder accountHolder);

    List<AccountHolderDetailsResponse> accountHoldersToAccountHolderDetailsResponseList(List<AccountHolder> accountHolders);

    @Mapping(target = "accountHolderId", source = "id")
    AccountHolderCreationResponse accountHolderToAccountCreationResponse(AccountHolder accountHolder);

    @Named("mapAccountsDetails")
    static Map<String, String> mapAccountsDetails(List<Account> accounts) {
        Map<String, String> currentAccounts = new HashMap<>();
        accounts.forEach(account -> currentAccounts.put(account.getIban(), account.getCurrency().toString()));
        return currentAccounts;
    }
}
