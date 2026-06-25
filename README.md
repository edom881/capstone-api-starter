# EasyShop E-Commerce API

EasyShop is a backend API for an e-commerce store. It supports the main shopping workflow: browsing products, filtering by category, managing a cart, maintaining a user profile, and checking out into an order.

The API includes the core features needed for a basic online store. Shoppers can browse the catalog, view product details, add products to a cart, update quantities, clear their cart, manage profile information, and place an order. Admin users can manage the store catalog by creating, updating, and deleting products and categories.

The project is built as a Spring Boot REST API backed by MySQL. It uses JWT authentication for protected routes, role-based authorization for admin-only features, Spring Data JPA for database access, service classes for business logic, and unit tests for controller and service behavior.
