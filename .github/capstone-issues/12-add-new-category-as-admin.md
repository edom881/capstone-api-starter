---
title: "Feature: Add new category as admin"
labels: feature, categories, admin, backend, required
---

**Phase 1 — CategoriesController.**

As an admin, I want to add a new category so that I can organize new types of products in the store.

### Tasks
- [ ] Add `@PostMapping` in `CategoriesController`.
- [ ] Add admin authorization if needed.
- [ ] Create or complete the service method to save a category.
- [ ] Test with an admin token: `POST http://localhost:8080/categories`.
- [ ] Test with a non-admin token and confirm it is denied.

### Done when
- Only users with `ADMIN` role can add categories
- Request body accepts category name and description
- New category is saved in the database
- Non-admin users are denied
