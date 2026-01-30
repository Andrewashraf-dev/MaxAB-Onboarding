package com.maxab.onboarding.service;

import com.maxab.onboarding.dto.OnboardingDTO;
import com.maxab.onboarding.entity.EmergencyContact;
import com.maxab.onboarding.entity.OnboardingRecord;
import com.maxab.onboarding.repository.OnboardingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardingRepository onboardingRepository;
    private final FileStorageService fileStorageService;
    private final ExcelService excelService;

    @Transactional
    public OnboardingRecord submitOnboarding(OnboardingDTO dto) throws IOException {

        // Check if National ID already exists
        if (onboardingRepository.existsByNationalId(dto.getNationalId())) {
            throw new IllegalArgumentException("National ID already exists in the system");
        }

        OnboardingRecord record = new OnboardingRecord();

        // Map personal information
        record.setNationalId(dto.getNationalId());
        record.setNationalIdExpiryDate(dto.getNationalIdExpiryDate());
        record.setFullNameArabic(dto.getFullNameArabic());
        record.setFullNameEnglish(dto.getFullNameEnglish());
        record.setGender(dto.getGender());
        record.setMaritalStatus(dto.getMaritalStatus());
        record.setDateOfBirth(dto.getDateOfBirth());
        record.setPlaceOfBirth(dto.getPlaceOfBirth());
        record.setReligion(dto.getReligion());
        record.setMobileNumber(dto.getMobileNumber());
        record.setRecommendedEmail(dto.getRecommendedEmail());
        record.setFullAddress(dto.getFullAddress());
        record.setCurrentAddress(dto.getCurrentAddress());

        // Store files
        String nationalId = dto.getNationalId();

        record.setPersonalPhotoPath(
                fileStorageService.storeFile(dto.getPersonalPhoto(), nationalId, "personal_photo")
        );
        record.setNationalIdFrontPath(
                fileStorageService.storeFile(dto.getNationalIdFront(), nationalId, "national_id_front")
        );
        record.setNationalIdBackPath(
                fileStorageService.storeFile(dto.getNationalIdBack(), nationalId, "national_id_back")
        );
        record.setCriminalRecordPath(
                fileStorageService.storeFile(dto.getCriminalRecord(), nationalId, "criminal_record")
        );
        record.setBirthCertificatePath(
                fileStorageService.storeFile(dto.getBirthCertificate(), nationalId, "birth_certificate")
        );
        record.setGraduationCertificatePath(
                fileStorageService.storeFile(dto.getGraduationCertificate(), nationalId, "graduation_cert")
        );
        record.setProofOfRegistrationPath(
                fileStorageService.storeFile(dto.getProofOfRegistration(), nationalId, "proof_of_reg")
        );
        record.setMilitaryCertificatePath(
                fileStorageService.storeFile(dto.getMilitaryCertificate(), nationalId, "military_cert")
        );
        record.setSyndicateCardPath(
                fileStorageService.storeFile(dto.getSyndicateCard(), nationalId, "syndicate_card")
        );

        // Map education details
        record.setUniversityName(dto.getUniversityName());
        record.setFaculty(dto.getFaculty());
        record.setGraduationYear(dto.getGraduationYear());
        record.setLastQualification(dto.getLastQualification());

        // Map employment & financial information
        record.setCibAccountNumber(dto.getCibAccountNumber());
        record.setSocialInsuranceNumber(dto.getSocialInsuranceNumber());

        // Map military information
        record.setMilitaryStatus(dto.getMilitaryStatus());

        // Add emergency contacts
        if (dto.getPrimaryContact() != null) {
            EmergencyContact primary = new EmergencyContact();
            primary.setName(dto.getPrimaryContact().getName());
            primary.setRelationship(dto.getPrimaryContact().getRelationship());
            primary.setContactNumber(dto.getPrimaryContact().getContactNumber());
            primary.setContactType("PRIMARY");
            record.addEmergencyContact(primary);
        }

        if (dto.getSecondaryContact() != null) {
            EmergencyContact secondary = new EmergencyContact();
            secondary.setName(dto.getSecondaryContact().getName());
            secondary.setRelationship(dto.getSecondaryContact().getRelationship());
            secondary.setContactNumber(dto.getSecondaryContact().getContactNumber());
            secondary.setContactType("SECONDARY");
            record.addEmergencyContact(secondary);
        }

        // Save to database
        OnboardingRecord savedRecord = onboardingRepository.save(record);

        // Append to Excel
        try {
            excelService.appendToExcel(savedRecord);
        } catch (IOException e) {
            // Log error but don't fail the transaction
            System.err.println("Failed to append to Excel: " + e.getMessage());
            throw e;
        }

        return savedRecord;
    }

    public OnboardingRecord getOnboardingById(Long id) {
        return onboardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Onboarding record not found"));
    }
}