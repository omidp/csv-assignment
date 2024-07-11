package com.csvloader.data.service;

import com.csvloader.data.domain.CsvRecordEntity;

import java.util.List;

public interface Parser {
    List<CsvRecordEntity> parse(byte[] bytes);
}
