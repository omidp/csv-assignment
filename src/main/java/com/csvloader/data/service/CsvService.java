package com.csvloader.data.service;

import com.csvloader.data.domain.CsvRecordEntity;
import com.csvloader.data.exception.CsvErrorException;
import com.csvloader.data.model.CsvFileAttributes;
import com.csvloader.data.model.CsvRecordResponse;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csvloader.data.validation.CsvValidation.isNameValid;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final SessionFactory sessionFactory;
    private final Parser parser;

    public List<CsvRecordResponse> fetchAll() {
        List<CsvRecordResponse> resultList = new ArrayList<>();
        sessionFactory.inTransaction(session -> {
            resultList.addAll(session.createSelectionQuery("from CsvRecordEntity cre", CsvRecordEntity.class)
                    //TODO : add pagination
//                            .setPage(Page.page())
                    .getResultList().stream().map(CsvRecordResponse::of).toList());
        });
        return resultList;
    }

    public CsvRecordResponse fetchByCode(String code) {
        return sessionFactory.fromTransaction(session -> {
            try {
                return CsvRecordResponse.of(session.createQuery(
                                "from CsvRecordEntity cre where cre.code = :code", CsvRecordEntity.class)
                        .setParameter("code", code).getSingleResult());
            } catch (NoResultException nre) {
                throw new CsvErrorException(code + " not found");
            }
        });
    }

    public void insert(byte[] bytes, String fileName) {
        Optional<String> validation = isNameValid().apply(new CsvFileAttributes(fileName));
        if (validation.isPresent()) {
            throw new CsvErrorException(validation.get());
        }
        //TODO: handle huge files
        sessionFactory.inTransaction(session -> parser.parse(bytes).forEach(session::persist));
    }

    public void deleteAll() {
        sessionFactory.inTransaction(session -> session.createMutationQuery("delete from CsvRecordEntity").executeUpdate());
    }

}
