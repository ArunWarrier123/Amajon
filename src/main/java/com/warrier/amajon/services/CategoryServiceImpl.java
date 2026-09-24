package com.warrier.amajon.services;


import com.warrier.amajon.models.Category;
import com.warrier.amajon.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> GetAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void CreateCategory(Category category) {
        categoryRepository.save(category);

    }

    @Override
    public ResponseEntity<String> DeleteCategoryByCategoryId(Integer categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            return new ResponseEntity<>("Category Not Found", HttpStatus.NOT_FOUND);
        } else {
            categoryRepository.delete(category.get());
            return new ResponseEntity<>("Category Deleted Successfully", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(Integer categoryId, Category category) {
        Optional<Category> originalCategory = categoryRepository.findById(categoryId);
        if (originalCategory.isEmpty()) {
            return new ResponseEntity<>("Category Not Found", HttpStatus.NOT_FOUND);
        } else {
            //update the category fields
            originalCategory.get().setCategoryName(category.getCategoryName());
            categoryRepository.save(originalCategory.get());
            return new ResponseEntity<>("Category Updated Successfully", HttpStatus.OK);
        }
    }
}
