package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest
{
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createFromCart_shouldCreateOrderLineItemsAndClearCart()
    {
        // arrange
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(createProduct(15, "Bluetooth Speaker", 129.99));
        item.setQuantity(2);
        cart.add(item);

        Profile profile = new Profile(3, "George", "Jetson", "800-555-9876",
                "george.jetson@email.com", "123 Birch Parkway", "Dallas", "TX", "75051");

        when(shoppingCartService.getByUserId(3)).thenReturn(cart);
        when(profileService.getByUserId(3)).thenReturn(profile);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(10);
            return order;
        });
        when(orderLineItemRepository.save(any(OrderLineItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Order actual = orderService.createFromCart(3);

        // assert
        assertEquals(10, actual.getOrderId(), "Saved order should include the new order id");
        assertEquals(3, actual.getUserId(), "Order should belong to the current user");
        assertEquals("123 Birch Parkway", actual.getAddress(), "Order should copy shipping address from profile");
        assertEquals(1, actual.getLineItems().size(), "Order should include one line item per cart item");
        assertEquals(15, actual.getLineItems().get(0).getProduct().getProductId(), "Line item should include product details");
        assertEquals(2, actual.getLineItems().get(0).getQuantity(), "Line item should include cart quantity");
        assertEquals(129.99, actual.getLineItems().get(0).getSalesPrice(), 0.001, "Line item should use current product price");
        verify(shoppingCartService).clearCart(3);
    }

    @Test
    public void createFromCart_withEmptyCart_shouldReturnNull()
    {
        // arrange
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartService.getByUserId(3)).thenReturn(cart);

        // act
        Order actual = orderService.createFromCart(3);

        // assert
        assertNull(actual, "Empty cart should not create an order");
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderLineItemRepository, never()).save(any(OrderLineItem.class));
        verify(shoppingCartService, never()).clearCart(3);
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
