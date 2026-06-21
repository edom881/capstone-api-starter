---
title: "Setup: Configure local database"
labels: setup, required
---

**Project setup — required.**

This issue tracks creation and seeding of the local MySQL database for the application.

### Tasks
- [ ] Run `database/create_database_easyshop.sql` against the local MySQL instance.
- [ ] Verify the `easyshop` database exists and contains the expected tables.
- [ ] Confirm sample data is present for users, profiles, categories, products, and shopping cart entries.
- [ ] Configure `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` for the application.

### Done when
- The `easyshop` database is created, seeded, and the app can connect to it.
