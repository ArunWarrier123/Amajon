package com.warrier.amajon.controllers;

import com.warrier.amajon.config.AppConstants;
import com.warrier.amajon.models.Category;
import com.warrier.amajon.payload.CategoryDTO;
import com.warrier.amajon.payload.CategoryResponse;
import com.warrier.amajon.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> GetCategories(
            @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize ,
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy" , defaultValue = AppConstants.CATEGORY_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder" , defaultValue = AppConstants.CATEGORY_SORT_ORDER, required = false) String sortOrder
            ) {
        return new ResponseEntity<>(categoryService.GetAllCategories(pageSize , pageNumber, sortBy , sortOrder) , HttpStatus.OK);
    }

    @PostMapping("/api/private/categories")
    public ResponseEntity<CategoryDTO> CreateCategory(@Valid @RequestBody CategoryDTO category) {
        return new ResponseEntity<>(categoryService.CreateCategory(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/private/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> DeleteCategory(@PathVariable Integer categoryId) {
        CategoryDTO deletedCategory = categoryService.DeleteCategoryByCategoryId(categoryId);
        return new ResponseEntity<>( deletedCategory, HttpStatus.OK );
    }


    @PutMapping("/api/private/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> UpdateCategory(@PathVariable Integer categoryId, @RequestBody CategoryDTO category) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId , category);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK) ;
    }
}
