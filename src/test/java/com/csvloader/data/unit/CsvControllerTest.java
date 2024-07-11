package com.csvloader.data.unit;

import com.csvloader.data.controller.CsvController;
import com.csvloader.data.controller.CsvExceptionController;
import com.csvloader.data.model.CsvRecordResponse;
import com.csvloader.data.service.CsvService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CsvControllerTest {

    private MockMvc mockMvc;
    public final ObjectMapper MAPPER = new ObjectMapper();
    @Mock
    private CsvService mockCsvService;


    @BeforeEach
    void setup() {
        MAPPER.registerModule(new JavaTimeModule());
        CsvController csvController = new CsvController(mockCsvService);
        mockMvc = MockMvcBuilders.standaloneSetup(csvController)
                .setControllerAdvice(new CsvExceptionController())
                .build();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockCsvService);
    }

    @Test
    void testFetchAll() throws Exception {
        var response = List.of(new CsvRecordResponse("1", "2", "3", "4", "5", Instant.now(), Instant.now(), 1));
        when(mockCsvService.fetchAll()).thenReturn(response);
        mockMvc.perform(get("/csv/fetch").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(response)))
        ;
    }

    @Test
    void testFetchByCode() throws Exception {
        var response = new CsvRecordResponse("1", "2", "3", "4", "5", Instant.now(), Instant.now(), 1);
        when(mockCsvService.fetchByCode(anyString())).thenReturn(response);
        mockMvc.perform(get("/csv/fetch/{code}", "1234").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(response)))
        ;
    }

    @Test
    void testDeleteAll() throws Exception {
        doNothing().when(mockCsvService).deleteAll();
        mockMvc.perform(delete("/csv/delete").contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    void testUpload() throws Exception {
        doNothing().when(mockCsvService).insert(any(), anyString());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/plain", "test data".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/csv/upload")
                .file(mockMultipartFile)).andExpect(status().isCreated())

        ;
    }


}
