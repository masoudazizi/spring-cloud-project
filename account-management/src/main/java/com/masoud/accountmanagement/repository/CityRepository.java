package com.masoud.accountmanagement.repository;

import com.masoud.accountmanagement.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
