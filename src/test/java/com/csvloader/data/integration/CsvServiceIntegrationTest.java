package com.csvloader.data.integration;

import com.csvloader.data.config.HibernateConfig;
import com.csvloader.data.service.CsvService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CsvServiceIntegrationTest.CsvTestConfigContext.class)
@TestPropertySource(
        properties = """
                  spring.datasource.url=jdbc:h2:mem:testdb
                  spring.datasource.driverClassName=org.h2.Driver
                  spring.datasource.username=sa
                  spring.datasource.password=password
                  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
                  spring.jpa.open-in-view=false
                  spring.jpa.show-sql=true
                  spring.jpa.generate-ddl=true
                """)
public class CsvServiceIntegrationTest {

    @Autowired
    private CsvService csvService;

    private String code;

    @BeforeEach
    void setup(){
        this.code = "271636001";
        String csv = """
                "source","codeListCode","code","displayValue","longDescription","fromDate","toDate","sortingPriority"
                "ZIB","ZIB001","271636001","Polsslag regelmatig","The long description is necessary","01-01-2019","","1"
                """;
        csvService.insert(csv.getBytes(), "test.csv");
    }

    @AfterEach
    void tearDown(){
        csvService.deleteAll();
    }

    @Test
    void testFetchAll() {
        assertEquals(1, csvService.fetchAll().size());
    }

    @Test
    void testFetchByCode() {
        assertEquals(this.code, csvService.fetchByCode(code).code());
    }

    @Import(HibernateConfig.class)
    @ComponentScan(basePackageClasses = CsvService.class)
    @EnableAutoConfiguration
    public static class CsvTestConfigContext {

    }

}
