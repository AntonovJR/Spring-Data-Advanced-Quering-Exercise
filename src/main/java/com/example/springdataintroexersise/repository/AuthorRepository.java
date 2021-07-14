package com.example.springdataintroexersise.repository;

import com.example.springdataintroexersise.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByFirstNameEndingWith(String symbol);

    List<Author> findAllByLastNameIsStartingWith(String symbol);
}
