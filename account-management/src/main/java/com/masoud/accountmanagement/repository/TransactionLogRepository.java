package com.masoud.accountmanagement.repository;

import com.masoud.accountmanagement.domain.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    Page<TransactionLog> findAllByTimeBetween(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

    Optional<TransactionLog> findByTrackingCode(String trackingCode);
}
