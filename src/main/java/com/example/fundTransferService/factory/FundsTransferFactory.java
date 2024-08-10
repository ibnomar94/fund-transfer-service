package com.example.fundTransferService.factory;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.respository.AccountRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class FundsTransferFactory {

    private final AccountRepository accountRepository;

    public FundsTransferOrder toFundsTransferOrder(FundsTransferRequest fundsTransferRequest) {
        FundsTransferOrder fundsTransfer = new FundsTransferOrder(fundsTransferRequest);
        fundsTransfer.setAccountToDebit(accountRepository.findByIban(fundsTransferRequest.getAccountToDebitIban()).orElseThrow(null));
        fundsTransfer.setAccountToCredit(accountRepository.findByIban(fundsTransferRequest.getAccountToCreditIban()).orElse(null));
        return fundsTransfer;
    }
}
