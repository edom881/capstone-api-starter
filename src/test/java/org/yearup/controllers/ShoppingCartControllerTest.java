package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.lang.reflect.Method;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingCartControllerTest
{
    @Test
    public void getCart_shouldReturnCartForLoggedInUser()
    {
        // arrange
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(createProduct(8, "Women's Jeans", 69.99));
        item.setQuantity(1);
        cart.add(item);

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(shoppingCartService.getByUserId(3)).thenReturn(cart);

        ShoppingCartController controller = new ShoppingCartController(shoppingCartService, userService);

        // act
        ShoppingCart actual = controller.getCart(principal);

        // assert
        assertEquals(1, actual.getItems().size(), "Controller should return the logged-in user's cart");
        assertEquals("Women's Jeans", actual.get(8).getProduct().getName(), "Cart should include product details");
    }

    @Test
    public void controller_shouldRequireLoggedInUser()
    {
        // act
        PreAuthorize annotation = ShoppingCartController.class.getAnnotation(PreAuthorize.class);

        // assert
        assertEquals("isAuthenticated()", annotation.value(), "Shopping cart should require a logged-in user");
    }

    @Test
    public void getCart_shouldHaveGetMapping() throws NoSuchMethodException
    {
        // arrange
        Method method = ShoppingCartController.class.getMethod("getCart", Principal.class);

        // act
        GetMapping annotation = method.getAnnotation(GetMapping.class);

        // assert
        assertEquals(1, annotation.value().length, "Get cart should have a GET mapping");
        assertEquals("", annotation.value()[0], "Get cart should map to /cart");
    }

    @Test
    public void addProductToCart_shouldReturnCreatedCartForLoggedInUser()
    {
        // arrange
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(createProduct(15, "Bluetooth Speaker", 129.99));
        item.setQuantity(1);
        cart.add(item);

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(shoppingCartService.addProduct(3, 15)).thenReturn(cart);

        ShoppingCartController controller = new ShoppingCartController(shoppingCartService, userService);

        // act
        ResponseEntity<ShoppingCart> response = controller.addProductToCart(15, principal);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Adding a product should return 201 Created");
        assertEquals(1, response.getBody().get(15).getQuantity(), "New cart item should have quantity 1");
    }

    @Test
    public void addProductToCart_shouldHavePostMapping() throws NoSuchMethodException
    {
        // arrange
        Method method = ShoppingCartController.class.getMethod("addProductToCart", int.class, Principal.class);

        // act
        PostMapping annotation = method.getAnnotation(PostMapping.class);

        // assert
        assertEquals(1, annotation.value().length, "Add product should have a POST mapping");
        assertEquals("products/{productId}", annotation.value()[0], "Add product should map to /cart/products/{productId}");
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
