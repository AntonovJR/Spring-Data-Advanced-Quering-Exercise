package com.example.springdataintroexersise.service;

import com.example.springdataintroexersise.model.entity.Author;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> findAllAuthorsNameEndsWith(String symbol);

    List<Author> findAllAuthorsLastNameStartWith(String symbol);

    List<String> findAllByCopiesSum();
}
