package com.warrier.amajon.services;


import com.warrier.amajon.exceptions.MyApiException;
import com.warrier.amajon.exceptions.ResourceNotFoundException;
import com.warrier.amajon.models.Category;
import com.warrier.amajon.payload.CategoryDTO;
import com.warrier.amajon.payload.CategoryResponse;
import com.warrier.amajon.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CategoryResponse GetAllCategories(Integer pageSize, Integer pageNumber, String sortBy , String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equals("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber , pageSize , sortByAndSortOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categoryList = categoryPage.getContent();
        if(categoryList.isEmpty()){
            throw new MyApiException("No categories found!");
        }
        List<CategoryDTO> categoryDTOList = categoryList.stream().map(category -> modelMapper.map(category , CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements((int) categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO CreateCategory(CategoryDTO category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new MyApiException("Category already exists with name " +  category.getCategoryName());
        }
        Category newCategoryCreated = modelMapper.map(category, Category.class);
        categoryRepository.save(newCategoryCreated);
        return modelMapper.map(newCategoryCreated, CategoryDTO.class);

    }

    @Override
    public CategoryDTO DeleteCategoryByCategoryId(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
        CategoryDTO deletedCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        categoryRepository.delete(category);
        return deletedCategoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Integer categoryId, CategoryDTO category) {
            Category originalCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));
            //update the category fields
            originalCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(originalCategory);
            return modelMapper.map(originalCategory, CategoryDTO.class);
    }
}
