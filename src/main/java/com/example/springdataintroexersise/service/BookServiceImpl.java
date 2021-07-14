package com.example.springdataintroexersise.service;

import com.example.springdataintroexersise.model.entity.*;
import com.example.springdataintroexersise.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .stream()
                .filter(row -> !row.isEmpty())
                .forEach(row -> {
                    String[] tokens = row.split("\\s+");
                    Book book = createBookFromFile(tokens);
                    this.bookRepository.save(book);
                });

    }


    @Override
    public List<String> findAllBooksByAgeRestrict(String ageRestrict) {
        AgeRestriction ageRestriction = AgeRestriction.valueOf(ageRestrict);
        return this.bookRepository.findAllByAgeRestriction(ageRestriction)
                .stream()
                .map((Book::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllGoldenBooksLessThan5000Copies(EditionType editionType, int copies) {
        return this.bookRepository.findAllByEditionTypeAndCopiesIsLessThan(editionType, copies)
                .stream()
                .map((Book::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByPriceNotInRange(BigDecimal lower, BigDecimal higher) {
        return this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(lower, higher).stream()
                .map(book -> String.format("%s $%.2f", book.getTitle(), book.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByNotReleasedIn(int year) {
        return this.bookRepository
                .findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate.of(year, 1, 1),
                        LocalDate.of(year, 12, 31))
                .stream()
                .map((Book::getTitle))
                .collect(Collectors.toList());
        //findAllByReleaseDateAfter();
    }

    @Override
    public List<String> findAllBooksBeforeDate(LocalDate date) {
        return this.bookRepository.findAllByReleaseDateBefore(date)
                .stream()
                .map(book -> String.format("%s %s %.2f", book.getTitle(), book.getEditionType(), book.getPrice()))
                .collect(Collectors.toList());
        //the title, the edition type and the price
    }

    @Override
    public List<String> findALlBooksWithTitleContainSymbols(String symbols) {
        return this.bookRepository.findAllByTitleContaining(symbols)
                .stream()
                .map(book -> String.format("%s", book.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorsLastNameStartsWith(List<Author> authors) {
        return this.bookRepository.findAllByAuthorIn(authors).stream()
                .map(book -> String.format("%s (%s %s)", book.getTitle(), book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer findCountOfBooksWithTitleLongerThan(int length) {
        return this.bookRepository.countBooksByTitleLength(length);
    }

    @Override
    public List<String> findBookByTitle(String title) {
        return this.bookRepository.findBookByTitleLike(title)
                .stream()
                .map(book -> String.format("%s %s %s %.2f", book.getTitle(), book.getEditionType(),
                        book.getAgeRestriction(), book.getPrice()))
                .collect(Collectors.toList());
    }


    private Book createBookFromFile(String[] tokens) {
        EditionType editionType = EditionType.values()[Integer.parseInt(tokens[0])];
        LocalDate releaseDate = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        int copies = Integer.parseInt(tokens[2]);
        BigDecimal price = new BigDecimal(tokens[3]);
        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(tokens[4])];
        String title = Arrays.stream(tokens).skip(5).collect(Collectors.joining(" "));
        Author author = this.authorService.getRandomAuthor();
        Set<Category> categories = this.categoryService.getRandomCategories();

        Book book = new Book();
        book.setTitle(title);
        book.setAgeRestriction(ageRestriction);
        book.setCopies(copies);
        book.setAuthor(author);
        book.setEditionType(editionType);
        book.setReleaseDate(releaseDate);
        book.setPrice(price);
        book.setCategorySet(categories);
        return book;
    }
}
