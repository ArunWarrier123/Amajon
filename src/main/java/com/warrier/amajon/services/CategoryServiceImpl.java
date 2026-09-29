package com.warrier.amajon.services;


import com.warrier.amajon.exceptions.MyApiException;
import com.warrier.amajon.exceptions.ResourceNotFoundException;
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
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new MyApiException("No categories found!");
        }
        return categoryList;
    }

    @Override
    public void CreateCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new MyApiException("Category already exists with name " +  category.getCategoryName());
        }
        categoryRepository.save(category);

    }

    @Override
    public String DeleteCategoryByCategoryId(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
            categoryRepository.delete(category);
            return "Category Deleted Successfully";

    }

    @Override
    public String updateCategory(Integer categoryId, Category category) {
            Category originalCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
            //update the category fields
            originalCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(originalCategory);
            return "Category Updated Successfully";

    }
}
