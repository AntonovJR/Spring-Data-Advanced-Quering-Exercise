package com.example.springdataintroexersise.repository;

import com.example.springdataintroexersise.model.entity.AgeRestriction;
import com.example.springdataintroexersise.model.entity.Author;
import com.example.springdataintroexersise.model.entity.Book;
import com.example.springdataintroexersise.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesIsLessThan(EditionType editionType, int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal low, BigDecimal high);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate start, LocalDate end);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDate);

    List<Book> findAllByTitleContaining(String symbols);

    List<Book> findAllByAuthorIn(List<Author> authors);

    @Query("SELECT count(b.id) from Book b where length(b.title)> :length")
    Integer countBooksByTitleLength(@Param(value = "length") int length);

    List<Book> findBookByTitleLike(String title);

}

