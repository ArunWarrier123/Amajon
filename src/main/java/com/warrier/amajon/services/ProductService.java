package com.warrier.amajon.services;

import com.warrier.amajon.models.Product;
import com.warrier.amajon.payload.ProductDTO;
import com.warrier.amajon.payload.ProductResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Integer categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<ProductResponse> getProductsByCategory(Integer categoryId);

    ResponseEntity<ProductResponse> getProductsBykeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO uploadProductImage(Long productId, MultipartFile image) throws IOException;
}
