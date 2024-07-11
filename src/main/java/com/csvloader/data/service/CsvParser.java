package com.csvloader.data.service;

import com.csvloader.data.domain.CsvRecordEntity;
import com.csvloader.data.exception.CsvErrorException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvParser implements Parser {

    @Override
    public List<CsvRecordEntity> parse(byte[] bytes) {
        List<CsvRecordEntity> entities = new ArrayList<>();
        Set<String> uniqueCodes = new HashSet<>();
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord.length == 8) {
                    CsvRecordEntity csvEntity = getCsvRecordEntity(nextRecord, uniqueCodes);
                    entities.add(csvEntity);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    private CsvRecordEntity getCsvRecordEntity(String[] nextRecord, Set<String> uniqueCodes) {
        CsvRecordEntity csvEntity = new CsvRecordEntity();
        csvEntity.setSource(nextRecord[0]);
        csvEntity.setCodeListCode(nextRecord[1]);
        csvEntity.setCode(nextRecord[2]);
        csvEntity.setDisplayValue(nextRecord[3]);
        csvEntity.setLongDescription(nextRecord[4]);
        csvEntity.setFromDate(convertToDate(nextRecord[5]));
        csvEntity.setToDate(convertToDate(nextRecord[6]));
        String priority = nextRecord[7];
        csvEntity.setSortingPriority((priority == null || priority.isEmpty()) ? null : Integer.parseInt(nextRecord[7]));
        if (uniqueCodes.contains(csvEntity.getCode())) {
            throw new CsvErrorException("Duplicate code found : " + csvEntity.getCode());
        }
        return csvEntity;
    }

    private Instant convertToDate(String input) {
        try {
            return new SimpleDateFormat("dd-MM-YYYY").parse(input).toInstant();
        } catch (ParseException e) {
            //Ignore
            log.info("Invalid date format {}", input);
        }
        return null;
    }
}
