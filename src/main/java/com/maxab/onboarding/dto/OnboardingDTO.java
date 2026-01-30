package com.maxab.onboarding.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Data
public class OnboardingDTO {

    // Personal Information
    private String nationalId;
    private LocalDate nationalIdExpiryDate;
    private String fullNameArabic;
    private String fullNameEnglish;
    private String gender;
    private String maritalStatus;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String religion;
    private String mobileNumber;
    private String recommendedEmail;
    private String fullAddress;
    private String currentAddress;

    // File uploads
    private MultipartFile personalPhoto;
    private MultipartFile nationalIdFront;
    private MultipartFile nationalIdBack;
    private MultipartFile criminalRecord;
    private MultipartFile birthCertificate;
    private MultipartFile graduationCertificate;
    private MultipartFile proofOfRegistration;
    private MultipartFile militaryCertificate;
    private MultipartFile syndicateCard;

    // Education Details
    private String universityName;
    private String faculty;
    private Integer graduationYear;
    private String lastQualification;

    // Employment & Financial Information
    private String cibAccountNumber;
    private String socialInsuranceNumber;

    // Military Information
    private String militaryStatus;

    // Emergency Contacts
    private EmergencyContactDTO primaryContact;
    private EmergencyContactDTO secondaryContact;

    @Data
    public static class EmergencyContactDTO {
        private String name;
        private String relationship;
        private String contactNumber;
    }
}