package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
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

    @Test
    public void addProduct_whenProductIsNotInCart_shouldCreateNewCartRow()
    {
        // arrange
        when(shoppingCartRepository.findByUserIdAndProductId(3, 15)).thenReturn(null);
        when(shoppingCartRepository.findByUserId(3)).thenReturn(Collections.singletonList(createCartItem(3, 15, 1)));
        when(productService.getById(15)).thenReturn(createProduct(15, "Bluetooth Speaker", 129.99));

        // act
        ShoppingCart actual = shoppingCartService.addProduct(3, 15);

        // assert
        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(shoppingCartRepository).save(captor.capture());
        assertEquals(3, captor.getValue().getUserId(), "New cart row should use the current user id");
        assertEquals(15, captor.getValue().getProductId(), "New cart row should use the selected product id");
        assertEquals(1, captor.getValue().getQuantity(), "New cart row should start with quantity 1");
        assertEquals(1, actual.get(15).getQuantity(), "Returned cart should include the new item");
    }

    @Test
    public void addProduct_whenProductIsAlreadyInCart_shouldIncreaseQuantity()
    {
        // arrange
        CartItem existing = createCartItem(3, 15, 2);

        when(shoppingCartRepository.findByUserIdAndProductId(3, 15)).thenReturn(existing);
        when(shoppingCartRepository.findByUserId(3)).thenReturn(Collections.singletonList(createCartItem(3, 15, 3)));
        when(productService.getById(15)).thenReturn(createProduct(15, "Bluetooth Speaker", 129.99));

        // act
        ShoppingCart actual = shoppingCartService.addProduct(3, 15);

        // assert
        verify(shoppingCartRepository).save(existing);
        assertEquals(3, existing.getQuantity(), "Existing cart row quantity should increase by 1");
        assertEquals(3, actual.get(15).getQuantity(), "Returned cart should include the updated quantity");
    }

    @Test
    public void updateQuantity_whenProductIsInCart_shouldSaveNewQuantity()
    {
        // arrange
        CartItem existing = createCartItem(3, 15, 1);

        when(shoppingCartRepository.findByUserIdAndProductId(3, 15)).thenReturn(existing);
        when(shoppingCartRepository.findByUserId(3)).thenReturn(Collections.singletonList(createCartItem(3, 15, 3)));
        when(productService.getById(15)).thenReturn(createProduct(15, "Bluetooth Speaker", 129.99));

        // act
        ShoppingCart actual = shoppingCartService.updateQuantity(3, 15, 3);

        // assert
        verify(shoppingCartRepository).save(existing);
        assertEquals(3, existing.getQuantity(), "Cart row quantity should be updated");
        assertEquals(3, actual.get(15).getQuantity(), "Returned cart should include the updated quantity");
    }

    @Test
    public void updateQuantity_whenProductIsNotInCart_shouldReturnNull()
    {
        // arrange
        when(shoppingCartRepository.findByUserIdAndProductId(3, 15)).thenReturn(null);

        // act
        ShoppingCart actual = shoppingCartService.updateQuantity(3, 15, 3);

        // assert
        assertNull(actual, "Missing cart item should return null");
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
