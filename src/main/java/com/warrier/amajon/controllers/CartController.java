package com.warrier.amajon.controllers;


import com.warrier.amajon.models.Cart;
import com.warrier.amajon.payload.CartDTO;
import com.warrier.amajon.repositories.CartRepository;
import com.warrier.amajon.services.CartService;
import com.warrier.amajon.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;


    @PostMapping("/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ){
        CartDTO cart = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOList = cartService.getAllCarts();

        return ResponseEntity.ok(cartDTOList);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart(){
        String emailId = authUtil.loggedInEmail();
        Cart cartByUser = cartRepository.findCartByEmail(emailId);
        CartDTO cartDTO = cartService.getCartByUser(emailId, cartByUser.getCartId());
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @PathVariable Long productId,
            @PathVariable String operation
    ){
        CartDTO cartDTO = cartService.updateProductQuantity(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return ResponseEntity.ok(cartDTO);
    }

}
