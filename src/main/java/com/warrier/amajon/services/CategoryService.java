package com.warrier.amajon.services;


import com.warrier.amajon.models.Category;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    List<Category> GetAllCategories();
    void CreateCategory(Category category);
    String DeleteCategoryByCategoryId(Integer categoryId);

    String updateCategory(Integer categoryId, Category category);
}
