package com.masoud.accountmanagement.repository;

import com.masoud.accountmanagement.domain.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
}
