---
title: "Feature: Clear shopping cart"
labels: optional, feature, cart, backend
---

**Optional Phase 3 — Shopping Cart.**

As a logged-in user, I want to clear my shopping cart so that I can remove all items at once.

### Tasks
- [ ] Add or complete `DELETE /cart`.
- [ ] Get the current logged-in user.
- [ ] Delete only that user's cart items.
- [ ] Test in Insomnia: `DELETE http://localhost:8080/cart`.
- [ ] Verify empty cart: `GET http://localhost:8080/cart`.

### Done when
- Only the current user's cart is cleared
- Shopping cart records are deleted from the database
- The endpoint is tested in Insomnia
