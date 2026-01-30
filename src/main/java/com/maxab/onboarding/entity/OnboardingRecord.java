package com.maxab.onboarding.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "onboarding")
@Data
public class OnboardingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    @Column(nullable = false, unique = true)
    private String nationalId;

    @Column(nullable = false)
    private LocalDate nationalIdExpiryDate;

    @Column(nullable = false)
    private String fullNameArabic;

    @Column(nullable = false)
    private String fullNameEnglish;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String maritalStatus;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String religion;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String recommendedEmail;

    @Column(length = 500)
    private String fullAddress;

    @Column(length = 500)
    private String currentAddress;

    private String personalPhotoPath;

    // Identification & Legal Documents
    private String nationalIdFrontPath;
    private String nationalIdBackPath;
    private String criminalRecordPath;
    private String birthCertificatePath;

    // Education Details
    private String universityName;
    private String faculty;
    private Integer graduationYear;
    private String lastQualification;
    private String graduationCertificatePath;
    private String proofOfRegistrationPath;

    // Employment & Financial Information
    private String cibAccountNumber;
    private String socialInsuranceNumber;

    // Military Information
    private String militaryStatus;
    private String militaryCertificatePath;

    // Syndicate Information
    private String syndicateCardPath;

    // Emergency Contacts
    @OneToMany(mappedBy = "onboardingRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate submissionDate;

    @PrePersist
    protected void onCreate() {
        submissionDate = LocalDate.now();
    }

    public void addEmergencyContact(EmergencyContact contact) {
        emergencyContacts.add(contact);
        contact.setOnboardingRecord(this);
    }
}