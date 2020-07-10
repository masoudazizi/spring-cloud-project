package com.masoud.accountmanagement.repository;

import com.masoud.accountmanagement.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}
