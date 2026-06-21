---
title: "Feature: Update product quantity in shopping cart"
labels: optional, bonus, cart, backend
---

**Optional Phase 3 — Shopping Cart.**

As a logged-in user, I want to update the quantity of a product in my cart so that I can control how many items I want to buy.

### Tasks
- [ ] Add or complete `PUT /cart/products/{productId}`.
- [ ] Read the quantity from the request body.
- [ ] Find the current user's cart item.
- [ ] Update the quantity.
- [ ] Save the updated cart row.
- [ ] Test in Insomnia: `PUT http://localhost:8080/cart/products/15` with `{ "quantity": 3 }`.

### Done when
- Request body accepts quantity
- Quantity updates only if the product already exists in the user's cart
- Updated quantity is saved in the database
