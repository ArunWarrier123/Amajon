package com.warrier.amajon.services;

import com.warrier.amajon.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartByUser(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantity(Long productId, int delete);
}
