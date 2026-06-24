package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController
{
    private final OrderService orderService;
    private final UserService userService;

    public OrdersController(OrderService orderService, UserService userService)
    {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("")
    public Order checkout(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        Order order = orderService.createFromCart(user.getId());

        if (order == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty.");

        return order;
    }
}
