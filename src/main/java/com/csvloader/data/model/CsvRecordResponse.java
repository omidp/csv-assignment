package com.csvloader.data.model;

import com.csvloader.data.domain.CsvRecordEntity;

import java.time.Instant;
import java.time.LocalDateTime;

public record CsvRecordResponse(String source, String codeListCode, String code, String displayValue,
                                String longDescription, Instant fromDate, Instant toDate,
                                Integer sortingPriority) {

    public static CsvRecordResponse of(CsvRecordEntity entity) {
        return new CsvRecordResponse(entity.getSource(), entity.getCodeListCode(), entity.getCode(),
                entity.getDisplayValue(), entity.getLongDescription(),
                entity.getFromDate(), entity.getToDate(), entity.getSortingPriority());
    }

}
