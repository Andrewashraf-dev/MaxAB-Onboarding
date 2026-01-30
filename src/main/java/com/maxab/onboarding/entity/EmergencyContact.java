package com.maxab.onboarding.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "emergency_contact")
@Data
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String relationship;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String contactType; // PRIMARY or SECONDARY

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_record_id", nullable = false)
    @JsonIgnore
    private OnboardingRecord onboardingRecord;
}