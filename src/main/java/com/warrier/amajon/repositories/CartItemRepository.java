package com.warrier.amajon.repositories;

import com.warrier.amajon.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?2 AND ci.product.id = ?1")
    CartItem findCartItembyProductIdAndCartId(Long productId, Long cartId);
}
