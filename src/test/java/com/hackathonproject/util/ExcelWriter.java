package com.hackathonproject.util;

import com.hackathonproject.runner.TestRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    private static final Logger log = LogManager.getLogger(ExcelWriter.class);

    private static Workbook workbook = new XSSFWorkbook();
    private static Sheet hospitalSheet;
    private static Sheet citiesSheet;

    private static String getFilePath() {
        String browser = TestRunner.getBrowserName() != null ? TestRunner.getBrowserName() : "default";
        return "target/generatedData_" + browser + ".xlsx";
    }

    private static Sheet getHospitalSheet() {
        if (hospitalSheet == null) {
            hospitalSheet = workbook.createSheet("Hospitals");
            hospitalSheet.createRow(0).createCell(0).setCellValue("Hospital Name");
        }
        return hospitalSheet;
    }

    private static Sheet getCitiesSheet() {
        if (citiesSheet == null) {
            citiesSheet = workbook.createSheet("Top Cities");
            citiesSheet.createRow(0).createCell(0).setCellValue("City Name");
        }
        return citiesSheet;
    }

    public static void writeHospitals(List<String> hospitals) {
        Sheet sheet = getHospitalSheet();
        int rowNum = sheet.getLastRowNum() + 1;
        for (String h : hospitals) sheet.createRow(rowNum++).createCell(0).setCellValue(h);
        log.info("Written {} hospitals to Excel", hospitals.size());
        save();
    }

    public static void writeCities(List<String> cities) {
        Sheet sheet = getCitiesSheet();
        int rowNum = sheet.getLastRowNum() + 1;
        for (String c : cities) sheet.createRow(rowNum++).createCell(0).setCellValue(c);
        log.info("Written {} cities to Excel", cities.size());
        save();
    }

    private static void save() {
        try (FileOutputStream fos = new FileOutputStream(getFilePath())) {
            workbook.write(fos);
            log.info("Saved Excel: {}", getFilePath());
        } catch (IOException e) {
            log.error("Failed to save Excel file: {}", e.getMessage());
        }
    }

    public static void cleanup() {
        try {
            if (workbook != null) workbook.close();
        } catch (IOException e) {
            log.error("Failed to close workbook: {}", e.getMessage());
        }
        // Reset for the next browser run in the same JVM
        workbook = new XSSFWorkbook();
        hospitalSheet = null;
        citiesSheet = null;
    }
}