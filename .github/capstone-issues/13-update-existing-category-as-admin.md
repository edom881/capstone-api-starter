---
title: "Feature: Update existing category as admin"
labels: feature, categories, admin, backend, required
---

**Phase 1 — CategoriesController.**

As an admin, I want to update a category so that I can correct or improve category information.

### Tasks
- [ ] Add `@PutMapping("/{id}")` in `CategoriesController`.
- [ ] Add admin authorization if needed.
- [ ] Create or complete the service method to update a category.
- [ ] Test with an admin token: `PUT http://localhost:8080/categories/1`.
- [ ] Confirm the category changed in the database: `GET http://localhost:8080/categories/1`.

### Done when
- Only users with `ADMIN` role can update categories
- Category name and description can be updated
- Updated category is saved in the database
- Non-admin users cannot update categories
