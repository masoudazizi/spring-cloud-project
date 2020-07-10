package com.masoud.accountmanagement.repository;

import com.masoud.accountmanagement.domain.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, Long> {
}
