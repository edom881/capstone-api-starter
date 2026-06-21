---
title: "Feature: View user profile"
labels: optional, feature, profile, backend
---

**Optional Phase 4 — User Profile.**

As a logged-in user, I want to view my profile so that I can see my saved account information.

### Tasks
- [ ] Create `ProfileController` if it does not exist.
- [ ] Add `GET /profile`.
- [ ] Use the current logged-in user to find the profile.
- [ ] Return the profile response.
- [ ] Test in Insomnia: `GET http://localhost:8080/profile` with valid JWT token.

### Done when
- Only logged-in users can view their profile
- The endpoint returns the current user's profile
- The controller calls the profile service
