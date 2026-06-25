package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService
{
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProfileService profileService;

    public OrderService(OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        ShoppingCartService shoppingCartService,
                        ProfileService profileService)
    {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }

    @Transactional
    public Order createFromCart(int userId)
    {
        ShoppingCart cart = shoppingCartService.getByUserId(userId);

        if (cart.getItems().isEmpty())
            return null;

        Profile profile = profileService.getByUserId(userId);
        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        order.setShippingAmount(0);

        Order savedOrder = orderRepository.save(order);
        List<OrderLineItem> lineItems = new ArrayList<>();

        // Each cart item becomes one order line item before the cart is cleared.
        for (ShoppingCartItem cartItem : cart.getItems().values())
        {
            Product product = cartItem.getProduct();
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setOrderId(savedOrder.getOrderId());
            lineItem.setProduct(product);
            lineItem.setQuantity(cartItem.getQuantity());
            lineItem.setSalesPrice(product.getPrice());
            lineItem.setDiscount(cartItem.getDiscountPercent());
            lineItems.add(orderLineItemRepository.save(lineItem));
        }

        savedOrder.setLineItems(lineItems);
        shoppingCartService.clearCart(userId);
        return savedOrder;
    }
}
