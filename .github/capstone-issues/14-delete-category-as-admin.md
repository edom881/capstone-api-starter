---
title: "Feature: Delete category as admin"
labels: feature, categories, admin, backend, required
---

**Phase 1 — CategoriesController.**

As an admin, I want to delete a category so that I can remove categories that are no longer needed.

### Tasks
- [ ] Add `@DeleteMapping("/{id}")` in `CategoriesController`.
- [ ] Add admin authorization if needed.
- [ ] Create or complete the service method to delete a category.
- [ ] Test with an admin token: `DELETE http://localhost:8080/categories/{id}`.
- [ ] Test with a non-admin token and confirm it is denied.
- [ ] Verify deletion: `GET http://localhost:8080/categories/{id}`.

### Done when
- Only users with `ADMIN` role can delete categories
- Deleted category is removed from the database
- Non-admin users are denied
- Endpoint returns the correct status response
