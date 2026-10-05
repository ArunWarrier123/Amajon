package com.warrier.amajon.controllers;


import com.warrier.amajon.config.AppConstants;
import com.warrier.amajon.payload.ProductDTO;
import com.warrier.amajon.payload.ProductResponse;
import com.warrier.amajon.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController()
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody  ProductDTO product,
            @PathVariable  Integer categoryId
    ){
        return new ResponseEntity<>(productService.addProduct(product , categoryId), HttpStatus.OK);
    }


    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize ,
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "sortBy" , defaultValue = AppConstants.PRODUCT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder" , defaultValue = AppConstants.CATEGORY_SORT_ORDER, required = false) String sortOrder
    ){
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(
            @PathVariable  Integer categoryId
    ){
        return productService.getProductsByCategory(categoryId);
    }


    @GetMapping("/products/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductByKeyword(
            @PathVariable  String keyword
    ){
        return productService.getProductsBykeyword(keyword);
    }


    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
        @PathVariable Long productId,
        @RequestBody  ProductDTO productDTO
    ){
        return new ResponseEntity<>(productService.updateProduct(productId, productDTO) , HttpStatus.OK) ;
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(
            @PathVariable Long productId
    ){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam MultipartFile image
    ) throws IOException {
        return new ResponseEntity<>(productService.uploadProductImage(productId, image), HttpStatus.OK);
    }

}
