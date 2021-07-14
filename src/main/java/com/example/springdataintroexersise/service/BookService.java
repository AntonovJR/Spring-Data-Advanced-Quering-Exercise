package com.example.springdataintroexersise.service;

import com.example.springdataintroexersise.model.entity.Author;
import com.example.springdataintroexersise.model.entity.EditionType;
import org.springframework.data.jpa.repository.Query;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<String> findAllBooksByAgeRestrict(String ageRestrict);

    List<String> findAllGoldenBooksLessThan5000Copies(EditionType editionType, int copies);

    List<String> findAllBooksByPriceNotInRange(BigDecimal lower, BigDecimal higher);

    List<String> findAllBooksByNotReleasedIn(int year);

    List<String> findAllBooksBeforeDate(LocalDate date);

    List<String> findALlBooksWithTitleContainSymbols(String symbols);

    List<String> findAllBooksByAuthorsLastNameStartsWith(List<Author> authors);

    Integer findCountOfBooksWithTitleLongerThan(int length);

    List<String> findBookByTitle(String title);
}
