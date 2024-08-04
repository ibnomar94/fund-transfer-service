package com.example.fundTransferService.business.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fundTransferService.business.model.AccountHolder;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
}
