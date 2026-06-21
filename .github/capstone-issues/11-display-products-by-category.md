---
title: "Feature: Display products by category"
labels: feature, categories, products, backend, required
---

**Phase 1 — CategoriesController.**

As a shopper, I want to view products in a selected category so that I can browse items more easily.

### Tasks
- [ ] Add `@GetMapping("/{id}/products")` in `CategoriesController`.
- [ ] Use a product service or repository method to get products by category.
- [ ] Return the list of matching products.
- [ ] Test in Insomnia: `GET http://localhost:8080/categories/1/products`.

### Done when
- The endpoint returns products for the selected category
- Empty categories return an empty list or appropriate response
- The endpoint is tested in Insomnia
