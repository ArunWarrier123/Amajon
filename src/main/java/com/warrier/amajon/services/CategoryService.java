package com.warrier.amajon.services;


import com.warrier.amajon.models.Category;
import com.warrier.amajon.payload.CategoryDTO;
import com.warrier.amajon.payload.CategoryResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    CategoryResponse GetAllCategories(Integer pageSize, Integer pageNumber ,  String sortBy , String sortOrder);
    CategoryDTO CreateCategory(CategoryDTO category);
    CategoryDTO DeleteCategoryByCategoryId(Integer categoryId);

    CategoryDTO updateCategory(Integer categoryId, CategoryDTO category);
}
