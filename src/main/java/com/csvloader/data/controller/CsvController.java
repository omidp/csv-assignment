package com.csvloader.data.controller;

import com.csvloader.data.model.CsvRecordResponse;
import com.csvloader.data.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/csv")
public class CsvController {

    private final CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        csvService.insert(file.getBytes(), file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<CsvRecordResponse>> fetch() {
        return ResponseEntity.ok(csvService.fetchAll());
    }

    @GetMapping("/fetch/{code}")
    public ResponseEntity<CsvRecordResponse> fetchByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(csvService.fetchByCode(code));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete() {
        csvService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
