package com.warrier.amajon.services;

import com.warrier.amajon.exceptions.MyApiException;
import com.warrier.amajon.exceptions.ResourceNotFoundException;
import com.warrier.amajon.models.Cart;
import com.warrier.amajon.models.CartItem;
import com.warrier.amajon.models.Product;
import com.warrier.amajon.payload.CartDTO;
import com.warrier.amajon.payload.ProductDTO;
import com.warrier.amajon.repositories.CartItemRepository;
import com.warrier.amajon.repositories.CartRepository;
import com.warrier.amajon.repositories.ProductRepository;
import com.warrier.amajon.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //check if cart exists for the user if so then use it or create one
        Cart cart = createCart();
        //retrieve product details
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId" , Math.toIntExact(productId)));
        //perform validations like stock exists etc
        CartItem cartItem = cartItemRepository.findCartItembyProductIdAndCartId(product.getProductId() , cart.getCartId());

        if(cartItem != null) {
            throw new MyApiException("Product" + product.getProductName() + " already exists in cart !");
        }
        if(product.getProductQuantity() == 0){
            throw new MyApiException("Product is out of stock");
        }
        if(product.getProductQuantity() < quantity){
            throw new MyApiException("please change quantity to under " + product.getProductQuantity());
        }

        //create cartItem and save and return updated cart
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getSpecialPrice());
        newCartItem.setDiscount(product.getDiscount());
        cartItemRepository.save(newCartItem);

        product.setProductQuantity(product.getProductQuantity());
        cart.setTotalPrice( cart.getTotalPrice() + product.getSpecialPrice()*product.getProductQuantity());
        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map((item) -> {
          ProductDTO productDTO = modelMapper.map(item,ProductDTO.class);
          productDTO.setProductQuantity(item.getQuantity());
          return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new MyApiException("No carts found");
        }
        List<CartDTO> cartDTOList = carts.stream().map(item -> {
            CartDTO cartDTO = modelMapper.map(item,CartDTO.class);
            List<ProductDTO> productDTOList = item.getItems().stream().map(p -> modelMapper.map(p,ProductDTO.class)).collect(Collectors.toList());
            cartDTO.setProducts(productDTOList);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOList;
    }

    @Override
    public CartDTO getCartByUser(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        cart.getItems().forEach(product -> product.setQuantity(product.getQuantity()));
        List<ProductDTO> productDTOList= cart.getItems().stream().map(p -> modelMapper.map(p,ProductDTO.class)).collect(Collectors.toList());
        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantity(Long productId, int update) {
        String emailId = authUtil.loggedInEmail();
        Cart cart =  cartRepository.findCartByEmail(emailId);
        CartItem cartItem = cartItemRepository.findCartItembyProductIdAndCartId(productId, cart.getCartId());
        cartItem.setQuantity( cartItem.getQuantity() + update );
        cartItemRepository.save(cartItem);
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if( userCart != null) {
            return userCart;
        }
        Cart newCart = new Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());
        cartRepository.save(newCart);
        return newCart;
    }
}
