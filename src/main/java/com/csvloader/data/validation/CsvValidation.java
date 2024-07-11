package com.csvloader.data.validation;

import com.csvloader.data.model.CsvFileAttributes;

import java.util.Optional;
import java.util.function.Function;

public interface CsvValidation extends Function<CsvFileAttributes, Optional<String>> {

    static CsvValidation isNameValid() {
        return csvFileAttributes -> csvFileAttributes.fileName().endsWith(".csv") ? Optional.empty() : Optional.of("Only csv files are accepted.");
    }

    default CsvValidation and(CsvValidation other) {
        return csvFileAttributes -> {
            Optional<String> validation = this.apply(csvFileAttributes);
            return validation.isEmpty() ? other.apply(csvFileAttributes) : validation;
        };
    }

}
