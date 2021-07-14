package com.example.springdataintroexersise.service;

import com.example.springdataintroexersise.model.entity.Author;
import com.example.springdataintroexersise.model.entity.Book;
import com.example.springdataintroexersise.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {


    private static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .stream()
                .filter(row -> !row.isEmpty())
                .forEach(row -> {
                    String[] tokens = row.split("\\s+");
                    Author author = new Author(tokens[0], tokens[1]);

                    this.authorRepository.save(author);
                });

    }

    @Override
    public Author getRandomAuthor() {
        long randomId = ThreadLocalRandom.current().nextLong(this.authorRepository.count() + 1);
        return this.authorRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<String> findAllAuthorsNameEndsWith(String symbol) {
        return this.authorRepository.findAllByFirstNameEndingWith(symbol)
                .stream()
                .map(author -> String.format("%s %s", author.getFirstName(), author.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> findAllAuthorsLastNameStartWith(String symbol) {
        return this.authorRepository.findAllByLastNameIsStartingWith(symbol);
    }

    @Override
    public List<String> findAllByCopiesSum() {
        return this.authorRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(author -> author.getBooks()
                        .stream()
                        .map(Book::getCopies)
                        .reduce(Integer::sum)
                        .orElse(0)))
                .map(author -> String.format("%s %s - %d", author.getFirstName(), author.getLastName(),
                        author.getBooks()
                                .stream()
                                .map(Book::getCopies)
                                .reduce(Integer::sum)
                                .orElse(0)))
                .collect(Collectors.toList());

    }


}






