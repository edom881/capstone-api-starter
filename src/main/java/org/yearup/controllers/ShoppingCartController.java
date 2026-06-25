package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService)
    {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    // This endpoint shows the shopping cart for the currently logged-in user.
    @GetMapping("")
    public ShoppingCart getCart(Principal principal)
    {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        // use the shoppingCartService to get all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable int productId, Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart cart = shoppingCartService.addProduct(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PutMapping("products/{productId}")
    public ShoppingCart updateProductQuantity(@PathVariable int productId,
                                              @RequestBody ShoppingCartItem item,
                                              Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart cart = shoppingCartService.updateQuantity(userId, productId, item.getQuantity());

        if (cart == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return cart;
    }

    @DeleteMapping("")
    public ShoppingCart clearCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.clearCart(userId);
    }
}
