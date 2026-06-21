---
title: "Test: Validate API with Insomnia"
labels: testing, required
---

**Testing with Insomnia — required.**

This issue tracks validating the e-commerce API using the Insomnia client.

### Tasks
- [ ] Create or import Insomnia requests for key endpoints such as `POST /login`, `POST /register`, `GET /categories`, `GET /products`, `GET /cart`, and cart modification endpoints.
- [ ] Use the JWT token returned by login in the `Authorization: Bearer <token>` header for protected routes.
- [ ] Verify successful responses and expected status codes for each request.
- [ ] Verify that invalid authentication and invalid request data return appropriate error responses.

### Done when
- The API is validated through Insomnia and protected routes work with JWT authentication.
