package com.warrier.amajon.controllers;

import com.warrier.amajon.models.Category;
import com.warrier.amajon.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> GetCategories() {
        return new ResponseEntity<>(categoryService.GetAllCategories() , HttpStatus.OK);
    }

    @PostMapping("/api/private/categories")
    public ResponseEntity<String> CreateCategory(@Valid @RequestBody Category category) {
        categoryService.CreateCategory(category);
        return new ResponseEntity<>("Category Created", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/private/categories/{categoryId}")
    public ResponseEntity<String> DeleteCategory(@PathVariable Integer categoryId) {
        String status = categoryService.DeleteCategoryByCategoryId(categoryId);
        return new ResponseEntity<>( status, HttpStatus.OK );
    }


    @PutMapping("/api/private/categories/{categoryId}")
    public ResponseEntity<String> UpdateCategory(@PathVariable Integer categoryId, @RequestBody Category category) {
        String status = categoryService.updateCategory(categoryId , category);
        return new ResponseEntity<>(status, HttpStatus.OK) ;
    }
}
