package com.maxab.onboarding.repository;

import com.maxab.onboarding.entity.OnboardingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OnboardingRepository extends JpaRepository<OnboardingRecord, Long> {
    Optional<OnboardingRecord> findByNationalId(String nationalId);
    boolean existsByNationalId(String nationalId);
}