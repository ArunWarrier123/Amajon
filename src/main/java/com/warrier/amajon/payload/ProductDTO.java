package com.warrier.amajon.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer productQuantity;
    private String productDescription;
    private String productImage;
    private Double discount;
    private Double specialPrice;
}
