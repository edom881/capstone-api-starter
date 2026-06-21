---
title: "Feature: Convert shopping cart to order"
labels: optional, feature, checkout, orders, backend
---

**Optional Phase 5 — Checkout.**

As a logged-in user, I want to check out my shopping cart so that I can place an order.

### Tasks
- [ ] Create order model/entity if needed.
- [ ] Create order line item model/entity if needed.
- [ ] Create repository, service, and controller for orders.
- [ ] Create an order from the current user's cart.
- [ ] Create order line items for each cart item.
- [ ] Clear the cart after order creation.
- [ ] Test in Insomnia: `POST http://localhost:8080/orders` after adding items to cart.

### Done when
- The current user's cart is converted into an order
- One order record is created
- Order line items are created for each cart item
- Shopping cart is cleared after checkout
- Order data is saved in the database
