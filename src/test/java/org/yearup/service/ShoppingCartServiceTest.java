package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.repository.ShoppingCartRepository;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest
{
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    public void getByUserId_shouldBuildCartWithProductDetails()
    {
        // arrange
        CartItem phoneRow = createCartItem(3, 1, 2);
        CartItem laptopRow = createCartItem(3, 2, 1);

        when(shoppingCartRepository.findByUserId(3)).thenReturn(Arrays.asList(phoneRow, laptopRow));
        when(productService.getById(1)).thenReturn(createProduct(1, "Smartphone", 499.99));
        when(productService.getById(2)).thenReturn(createProduct(2, "Laptop", 899.99));

        // act
        ShoppingCart actual = shoppingCartService.getByUserId(3);

        // assert
        assertEquals(2, actual.getItems().size(), "Cart should contain two products");
        assertEquals(2, actual.get(1).getQuantity(), "Phone quantity should come from the cart row");
        assertEquals("Laptop", actual.get(2).getProduct().getName(), "Cart item should include product details");
        assertEquals(1899.97, actual.getTotal(), 0.001, "Cart total should include all line totals");
    }

    @Test
    public void getByUserId_withNoRows_shouldReturnEmptyCart()
    {
        // arrange
        when(shoppingCartRepository.findByUserId(3)).thenReturn(Collections.emptyList());

        // act
        ShoppingCart actual = shoppingCartService.getByUserId(3);

        // assert
        assertEquals(0, actual.getItems().size(), "Empty cart should not contain items");
        assertEquals(0, actual.getTotal(), 0.001, "Empty cart total should be zero");
    }

    private CartItem createCartItem(int userId, int productId, int quantity)
    {
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    private Product createProduct(int id, String name, double price)
    {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
