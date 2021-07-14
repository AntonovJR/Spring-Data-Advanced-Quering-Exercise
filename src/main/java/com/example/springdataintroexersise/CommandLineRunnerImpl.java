package com.example.springdataintroexersise;

import com.example.springdataintroexersise.model.entity.Author;
import com.example.springdataintroexersise.model.entity.EditionType;
import com.example.springdataintroexersise.service.AuthorService;
import com.example.springdataintroexersise.service.BookService;
import com.example.springdataintroexersise.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;
    private final CategoryService categoryService;

    public CommandLineRunnerImpl(AuthorService authorService, BookService bookService, BufferedReader bufferedReader, CategoryService categoryService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
        System.out.println("Please enter exercise number: ");
        int exNumber = Integer.parseInt(bufferedReader.readLine());
        switch (exNumber) {
            case 1:
                printBooksByAgeRestriction();
                break;
            case 2:
                printGoldenBooks();
                break;
            case 3:
                printBooksPriceRangeIn();
                break;
            case 4:
                printBooksNotReleasedIn();
                break;
            case 5:
                printBooksReleasedBefore();
                break;
            case 6:
                printAuthorsWithAuthorNamesEndsWith();
                break;
            case 7:
                printBooksWithTitleContainSymbols();
                break;
            case 8:
                printBooksWithAuthorLastNameStartsWith();
                break;
            case 9:
                printCountOfBooksWithTitleLongerThan();
                break;
            case 10:
                printTotalCopiesByAuthor();
                break;
            case 11:
                printBookDetailsByName();
                break;
            default:
                System.out.println("Please enter number between 1 and 11.");

        }

    }

    private void printBookDetailsByName() throws IOException {
        System.out.println("Please enter book Title: ");
        String title = bufferedReader.readLine();
        bookService.findBookByTitle(title).forEach(System.out::println);
    }

    private void printTotalCopiesByAuthor() {
        List<String> allByCopiesSum = authorService.findAllByCopiesSum();
        for (int i = allByCopiesSum.size() - 1; i >= 0; i--) {
            System.out.println(allByCopiesSum.get(i));

        }
    }

    private void printCountOfBooksWithTitleLongerThan() throws IOException {
        System.out.println("Please enter an integer: ");
        int length = Integer.parseInt(bufferedReader.readLine());
        int count = this.bookService.findCountOfBooksWithTitleLongerThan(length);
        System.out.printf("There are %d books with longer title than %d symbols.%n", count, length);


    }

    private void printBooksWithAuthorLastNameStartsWith() throws IOException {
        String symbols = askForData();
        List<Author> allAuthorsLastNameStartWith = authorService.findAllAuthorsLastNameStartWith(symbols);
        bookService.findAllBooksByAuthorsLastNameStartsWith(allAuthorsLastNameStartWith).forEach(System.out::println);

    }

    private String askForData() throws IOException {
        System.out.println("Please enter symbols: ");
        return bufferedReader.readLine();
    }

    private void printBooksWithTitleContainSymbols() throws IOException {
        String symbols = askForData();
        bookService.findALlBooksWithTitleContainSymbols(symbols).forEach(System.out::println);
    }

    private void printAuthorsWithAuthorNamesEndsWith() throws IOException {
        String symbols = askForData();
        authorService.findAllAuthorsNameEndsWith(symbols).forEach(System.out::println);

    }

    private void printBooksReleasedBefore() throws IOException {
        System.out.println("Please enter a date in format dd-MM-yyyy: ");
        String[] dateString = bufferedReader.readLine().split("-");
        LocalDate date = LocalDate.of(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[0]));
        bookService.findAllBooksBeforeDate(date).forEach(System.out::println);

    }

    private void printBooksNotReleasedIn() throws IOException {
        System.out.println("Please enter an year: ");
        int year = Integer.parseInt(bufferedReader.readLine());
        bookService.findAllBooksByNotReleasedIn(year).forEach(System.out::println);

    }

    private void printBooksPriceRangeIn() {
        BigDecimal lower = new BigDecimal(5);
        BigDecimal higher = new BigDecimal(40);
        bookService.findAllBooksByPriceNotInRange(lower, higher).forEach(System.out::println);


    }

    private void printGoldenBooks() {
        EditionType editionType = EditionType.valueOf("GOLD");
        int copies = 5000;
        bookService.findAllGoldenBooksLessThan5000Copies(editionType, copies).forEach(System.out::println);
    }

    private void printBooksByAgeRestriction() throws IOException {
        System.out.println("Please enter age parameter: ");
        String ageRestrict = bufferedReader.readLine().toUpperCase(Locale.ROOT);
        bookService.findAllBooksByAgeRestrict(ageRestrict).forEach(System.out::println);
    }

}
