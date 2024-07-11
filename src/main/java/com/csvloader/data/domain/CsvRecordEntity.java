package com.csvloader.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "csv_record")
public class CsvRecordEntity {
    private String source;
    private String codeListCode;
    @NaturalId
    @Id
    @Column(unique = true)
    private String code;
    private String displayValue;
    private String longDescription;
    private Instant fromDate;
    private Instant toDate;
    private Integer sortingPriority;
}
