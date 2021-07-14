package com.example.springdataintroexersise.service;

import com.example.springdataintroexersise.model.entity.Category;
import com.example.springdataintroexersise.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.activation.FileDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE_PATH = "src/main/resources/files/categories.txt";
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(row -> !row.isEmpty())
                .forEach(row -> {
                    Category category = new Category(row);

                    this.categoryRepository.save(category);
                });

    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categorySet = new HashSet<>();
        int randInt = ThreadLocalRandom.current().nextInt(1, 3);
        long carRepoCount = this.categoryRepository.count();

        for (int i = 0; i < randInt; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(carRepoCount);
            Category category = this.categoryRepository.findById(randomId).orElse(null);
            categorySet.add(category);
        }


        return categorySet;
    }
}
