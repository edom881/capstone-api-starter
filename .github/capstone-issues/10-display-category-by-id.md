---
title: "Feature: Display category by ID"
labels: feature, categories, backend, required
---

**Phase 1 — CategoriesController.**

As a shopper, I want to view one category by ID so that I can see details for a specific category.

### Tasks
- [ ] Add `@GetMapping("/{id}")` in `CategoriesController`.
- [ ] Create or complete the service method to find category by ID.
- [ ] Return the category when it exists.
- [ ] Handle missing category IDs correctly.
- [ ] Test in Insomnia: `GET http://localhost:8080/categories/1` and `GET http://localhost:8080/categories/99999`.

### Done when
- A valid category ID returns the correct category
- An invalid category ID returns an appropriate response
- The endpoint is tested in Insomnia
