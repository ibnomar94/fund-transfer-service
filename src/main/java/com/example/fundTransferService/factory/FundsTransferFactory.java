package com.example.fundTransferService.factory;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.dto.response.FundsTransferResponse;
import com.example.fundTransferService.business.respository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FundsTransferFactory {

    private final AccountRepository accountRepository;

    public FundsTransferOrder toFundsTransferOrder(FundsTransferRequest fundsTransferRequest) {
        FundsTransferOrder fundsTransfer = new FundsTransferOrder(fundsTransferRequest);
        fundsTransfer.setAccountToDebit(accountRepository.findByIban(fundsTransferRequest.getAccountToDebitIban()).orElseThrow(null));
        fundsTransfer.setAccountToCredit(accountRepository.findByIban(fundsTransferRequest.getAccountToCreditIban()).orElse(null));
        return fundsTransfer;
    }

    public FundsTransferResponse toFundsTransferResponse(FundsTransferOrder fundsTransferOrder, String transactionId) {
        FundsTransferResponse fundsTransferResponse = new FundsTransferResponse();
        fundsTransferResponse.setExchangeFee(fundsTransferOrder.getExchangeFee());
        fundsTransferResponse.setExchangeRate(fundsTransferOrder.getExchangeRate());
        fundsTransferResponse.setTransactionId(transactionId);
        return fundsTransferResponse;
    }
}
