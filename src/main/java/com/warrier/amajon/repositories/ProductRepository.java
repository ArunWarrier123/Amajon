package com.warrier.amajon.repositories;

import com.warrier.amajon.models.Category;
import com.warrier.amajon.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Product findByProductNameIgnoreCase(String productName);
}
