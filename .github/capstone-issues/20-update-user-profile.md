---
title: "Feature: Update user profile"
labels: optional, feature, profile, backend
---

**Optional Phase 4 — User Profile.**

As a logged-in user, I want to update my profile so that my contact and shipping information stays current.

### Tasks
- [ ] Add `PUT /profile`.
- [ ] Create or complete the profile update service method.
- [ ] Make sure the update belongs to the current logged-in user.
- [ ] Save the updated profile.
- [ ] Test in Insomnia: `PUT http://localhost:8080/profile` with valid JWT token.
- [ ] Verify changes: `GET http://localhost:8080/profile`.

### Done when
- Only logged-in users can update their profile
- First name, last name, phone, email, address, city, state, and zip can be updated
- Updated profile is saved in the database
