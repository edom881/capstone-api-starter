package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.lang.reflect.Method;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrdersControllerTest
{
    @Test
    public void checkout_shouldCreateOrderForLoggedInUser()
    {
        // arrange
        OrderService orderService = mock(OrderService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");
        Order order = new Order();
        order.setOrderId(10);
        order.setUserId(3);

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(orderService.createFromCart(3)).thenReturn(order);

        OrdersController controller = new OrdersController(orderService, userService);

        // act
        Order actual = controller.checkout(principal);

        // assert
        assertEquals(10, actual.getOrderId(), "Checkout should return the created order");
        assertEquals(3, actual.getUserId(), "Order should belong to the logged-in user");
    }

    @Test
    public void checkout_withEmptyCart_shouldThrowBadRequest()
    {
        // arrange
        OrderService orderService = mock(OrderService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(orderService.createFromCart(3)).thenReturn(null);

        OrdersController controller = new OrdersController(orderService, userService);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.checkout(principal));

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode(), "Empty cart should return 400");
    }

    @Test
    public void controller_shouldRequireLoggedInUser()
    {
        // act
        PreAuthorize annotation = OrdersController.class.getAnnotation(PreAuthorize.class);

        // assert
        assertEquals("isAuthenticated()", annotation.value(), "Checkout should require a logged-in user");
    }

    @Test
    public void checkout_shouldHavePostMapping() throws NoSuchMethodException
    {
        // arrange
        Method method = OrdersController.class.getMethod("checkout", Principal.class);

        // act
        PostMapping annotation = method.getAnnotation(PostMapping.class);

        // assert
        assertEquals(1, annotation.value().length, "Checkout should have a POST mapping");
        assertEquals("", annotation.value()[0], "Checkout should map to /orders");
    }
}
