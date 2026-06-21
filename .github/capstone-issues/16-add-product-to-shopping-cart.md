---
title: "Feature: Add product to shopping cart"
labels: optional, feature, cart, backend
---

**Optional Phase 3 — Shopping Cart.**

As a logged-in user, I want to add a product to my cart so that I can buy it later.

### Tasks
- [ ] Create or complete the service method to add a product to the cart.
- [ ] Check if the product already exists in the user's cart.
- [ ] Insert a new cart row when the product is not in the cart.
- [ ] Increase quantity when the product is already in the cart.
- [ ] Test in Insomnia: `POST http://localhost:8080/cart/products/15` twice and verify quantity increases.

### Done when
- If the product is not already in the cart, it is added with quantity 1
- If the product is already in the cart, quantity increases by 1
- Cart item is saved in the database
- Only logged-in users can add items
