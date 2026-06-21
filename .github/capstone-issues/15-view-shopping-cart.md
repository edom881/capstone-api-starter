---
title: "Feature: View shopping cart"
labels: optional, feature, cart, backend
---

**Optional Phase 3 — Shopping Cart.**

As a logged-in user, I want to view my shopping cart so that I can see the products I plan to buy.

### Tasks
- [ ] Use the logged-in username from the security context.
- [ ] Find the current user's user ID.
- [ ] Load cart rows for that user.
- [ ] Look up full product details for each cart item.
- [ ] Build and return the shopping cart response.
- [ ] Test in Insomnia: `GET http://localhost:8080/cart` with JWT token.

### Done when
- Only logged-in users can view the cart
- Cart shows product details, quantity, line total, and total
- Cart data is loaded for the current logged-in user
- Unauthenticated requests are denied
