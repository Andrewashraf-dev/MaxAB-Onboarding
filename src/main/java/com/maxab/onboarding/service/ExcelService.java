package com.maxab.onboarding.service;

import com.maxab.onboarding.entity.EmergencyContact;
import com.maxab.onboarding.entity.OnboardingRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelService {

    @Value("${excel.template-path:onboarding_template.xlsx}")
    private String templatePath;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void appendToExcel(OnboardingRecord record) throws IOException {
        Workbook workbook;
        Sheet sheet;

        try (FileInputStream fis = new FileInputStream(templatePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        }

        // Find the next empty row
        int lastRowNum = sheet.getLastRowNum();
        int newRowNum = lastRowNum + 1;
        Row row = sheet.createRow(newRowNum);

        // Map data to columns (adjust column indices based on your template)
        int col = 0;

        // Personal Information
        createCell(row, col++, record.getNationalId());
        createCell(row, col++, formatDate(record.getNationalIdExpiryDate()));
        createCell(row, col++, record.getFullNameArabic());
        createCell(row, col++, record.getFullNameEnglish());
        createCell(row, col++, record.getGender());
        createCell(row, col++, record.getMaritalStatus());
        createCell(row, col++, formatDate(record.getDateOfBirth()));
        createCell(row, col++, record.getPlaceOfBirth());
        createCell(row, col++, record.getReligion());
        createCell(row, col++, record.getMobileNumber());
        createCell(row, col++, record.getRecommendedEmail());
        createCell(row, col++, record.getFullAddress());
        createCell(row, col++, record.getCurrentAddress());
        createCell(row, col++, record.getPersonalPhotoPath());

        // Identification & Legal Documents
        createCell(row, col++, record.getNationalIdFrontPath());
        createCell(row, col++, record.getNationalIdBackPath());
        createCell(row, col++, record.getCriminalRecordPath());
        createCell(row, col++, record.getBirthCertificatePath());

        // Education Details
        createCell(row, col++, record.getUniversityName());
        createCell(row, col++, record.getFaculty());
        createCell(row, col++, record.getGraduationYear());
        createCell(row, col++, record.getLastQualification());
        createCell(row, col++, record.getGraduationCertificatePath());
        createCell(row, col++, record.getProofOfRegistrationPath());

        // Employment & Financial Information
        createCell(row, col++, record.getCibAccountNumber());
        createCell(row, col++, record.getSocialInsuranceNumber());

        // Military Information
        createCell(row, col++, record.getMilitaryStatus());
        createCell(row, col++, record.getMilitaryCertificatePath());

        // Syndicate Information
        createCell(row, col++, record.getSyndicateCardPath());

        // Emergency Contacts
        EmergencyContact primary = record.getEmergencyContacts().stream()
                .filter(ec -> "PRIMARY".equals(ec.getContactType()))
                .findFirst()
                .orElse(null);

        EmergencyContact secondary = record.getEmergencyContacts().stream()
                .filter(ec -> "SECONDARY".equals(ec.getContactType()))
                .findFirst()
                .orElse(null);

        if (primary != null) {
            createCell(row, col++, primary.getName());
            createCell(row, col++, primary.getRelationship());
            createCell(row, col++, primary.getContactNumber());
        } else {
            col += 3;
        }

        if (secondary != null) {
            createCell(row, col++, secondary.getName());
            createCell(row, col++, secondary.getRelationship());
            createCell(row, col++, secondary.getContactNumber());
        } else {
            col += 3;
        }

        // Submission Date
        createCell(row, col++, formatDate(record.getSubmissionDate()));

        // Write to file
        try (FileOutputStream fos = new FileOutputStream(templatePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }

    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
}