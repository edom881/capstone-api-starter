# EasyShop E-Commerce API

<!-- This README explains the project features, setup steps, API endpoints, and interesting code for the capstone. -->

EasyShop is a backend API for an e-commerce store. It supports the main shopping workflow from product browsing to checkout, including category browsing, shopping cart management, profile management, and order creation.

The API includes the core features needed for a basic online store. Shoppers can browse the catalog, view product details, add products to a cart, update quantities, clear their cart, manage profile information, and place an order. Admin users can manage the store catalog by creating, updating, and deleting products and categories.

The project is built with Spring Boot and MySQL. It uses JWT authentication for protected routes, role-based authorization for admin-only features, Spring Data JPA for database access, service classes for business logic, and unit tests for controller and service behavior.

## Project Summary

### Shopper Features

EasyShop supports a complete shopping workflow:

- Browse available products
- View products by category
- View details for one product or category
- Add items to a cart
- Change cart quantities
- Clear the cart
- Save profile and shipping information
- Convert a cart into an order
- Store order and order line item records in the database

### Admin Features

Admin users can manage the store catalog:

- Add new products
- Update product details such as price, description, image, featured status, and stock
- Delete products
- Create, update, and delete categories

### Current Scope

This project focuses on the backend API. The main workflows can be tested with Swagger UI, Insomnia, Postman, or another HTTP client. A frontend is not required to test the API.

The database stores users, profiles, categories, products, shopping cart rows, orders, and order line items. The API uses the logged-in user's JWT token to make sure cart, profile, and checkout actions apply to the correct account.

## Technologies Used

- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- JWT authentication
- Spring Data JPA
- MySQL
- H2 for tests
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito

## Features

- User registration and login
- JWT-based authentication
- Product search and product lookup by ID
- Admin product create, update, and delete
- Category list and category lookup by ID
- Product browsing by category
- Admin category create, update, and delete
- Logged-in user shopping cart
- Add products to cart
- Update cart item quantity
- Clear shopping cart
- View and update user profile
- Checkout cart into an order with order line items

## User Roles

| Role | What the user can do |
| --- | --- |
| Shopper | Browse products, view categories, manage cart, update profile, and checkout |
| Admin | Manage products and categories |

## Main User Flows

### Shopper Checkout Flow

1. The user logs in and receives a JWT token.
2. The user browses all products or filters products by category.
3. The user adds one or more products to the cart.
4. The user updates item quantities if needed.
5. The user views or updates profile and shipping information.
6. The user checks out with `POST /orders`.
7. The API creates an order record.
8. The API creates one order line item for each cart item.
9. The API clears the user's shopping cart after checkout.

### Admin Catalog Flow

1. The admin logs in and receives a JWT token.
2. The admin creates categories to organize products.
3. The admin adds products to the catalog.
4. The admin updates product data such as stock, price, description, image, and featured status.
5. The admin updates or deletes categories and products as needed.
6. Catalog changes are saved in the database.

## Setup Instructions

1. Clone the repository.

2. Create the MySQL database by running one of the SQL scripts in the `database` folder. The default database name is `easyshop`.

   ```sql
   source database/create_database_easyshop.sql;
   ```

3. Set the database environment variables.

   ```powershell
   $env:DB_USERNAME="your_mysql_username"
   $env:DB_PASSWORD="your_mysql_password"
   ```

   Optional:

   ```powershell
   $env:DB_NAME="easyshop"
   ```

4. Run the API.

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. Open Swagger UI.

   ```text
   http://localhost:8080/swagger-ui.html
   ```

## Test Users

The database seed script includes these users:

| Username | Password | Role |
| --- | --- | --- |
| `user` | `password` | `ROLE_USER` |
| `admin` | `password` | `ROLE_ADMIN` |
| `george` | `password` | `ROLE_USER` |

## Authentication

Login endpoint:

```http
POST /login
```

Example body:

```json
{
  "username": "admin",
  "password": "password"
}
```

Use the returned token as a Bearer token when calling protected endpoints.

```text
Authorization: Bearer <token>
```

## API Endpoint Summary

### Authentication

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/login` | Login and receive JWT token |
| `POST` | `/register` | Register a new user |

### Products

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/products` | Search or list products |
| `GET` | `/products/{id}` | Get one product |
| `POST` | `/products` | Add product as admin |
| `PUT` | `/products/{id}` | Update product as admin |
| `DELETE` | `/products/{id}` | Delete product as admin |

### Categories

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/categories` | List all categories |
| `GET` | `/categories/{id}` | Get one category |
| `GET` | `/categories/{id}/products` | List products in a category |
| `POST` | `/categories` | Add category as admin |
| `PUT` | `/categories/{id}` | Update category as admin |
| `DELETE` | `/categories/{id}` | Delete category as admin |

### Shopping Cart

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/cart` | View current user's cart |
| `POST` | `/cart/products/{productId}` | Add product to cart |
| `PUT` | `/cart/products/{productId}` | Update cart item quantity |
| `DELETE` | `/cart` | Clear current user's cart |

### Profile

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/profile` | View current user's profile |
| `PUT` | `/profile` | Update current user's profile |

### Orders

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/orders` | Convert current user's cart into an order |

## Example Requests

Add a product to the cart:

```http
POST /cart/products/15
Authorization: Bearer <token>
```

Update cart quantity:

```http
PUT /cart/products/15
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "quantity": 3
}
```

Checkout:

```http
POST /orders
Authorization: Bearer <token>
```

## Screenshots

Add screenshots from the running project before final submission.
These screenshots show each EasyShop project phase tested in Insomnia, plus the frontend app running against the backend API.
 |

<img width="958" height="515" alt="Screenshot 2026-06-26 124424" src="https://github.com/user-attachments/assets/cfb9bb18-4aed-45db-9799-2e5f15d9cb7a" />
<img width="959" height="503" alt="Screenshot 2026-06-26 125855" src="https://github.com/user-attachments/assets/5d9bb48b-ef10-4d19-a784-6e892576da5d" />
<img width="954" height="484" alt="Screenshot 2026-06-26 125935" src="https://github.com/user-attachments/assets/158038c2-3d23-4c17-9d5a-2f211b24cef6" />
<img width="955" height="518" alt="Screenshot 2026-06-26 130014" src="https://github.com/user-attachments/assets/ab254041-4b2b-4a60-93b5-21334160ea5c" />
<img width="952" height="499" alt="Screenshot 2026-06-26 130039" src="https://github.com/user-attachments/assets/4791bf21-f75a-47bf-9f09-fec8f165ce5e" />
<img width="949" height="548" alt="Screenshot 2026-06-26 130442" src="https://github.com/user-attachments/assets/ab3b5fe0-c9cf-437e-906e-ad7982ff22d4" />
<img width="956" height="532" alt="Screenshot 2026-06-26 130506" src="https://github.com/user-attachments/assets/482541ff-b52d-461b-81a7-43b91ed4ff72" />
<img width="953" height="524" alt="Screenshot 2026-06-26 130705" src="https://github.com/user-attachments/assets/e7bbc73b-e90f-48cb-9cc2-5cea4bd2226e" />



## Interesting Code

One interesting part of this project is the checkout flow in `OrderService`.

The service gets the logged-in user's cart, creates an order using the user's profile address, creates one order line item for each cart item, and then clears the cart after checkout.

```java
@Transactional
public Order createFromCart(int userId)
{
    ShoppingCart cart = shoppingCartService.getByUserId(userId);

    if (cart.getItems().isEmpty())
        return null;

    Profile profile = profileService.getByUserId(userId);
    Order order = new Order();
    order.setUserId(userId);
    order.setDate(LocalDateTime.now());
    order.setAddress(profile.getAddress());
    order.setCity(profile.getCity());
    order.setState(profile.getState());
    order.setZip(profile.getZip());

    Order savedOrder = orderRepository.save(order);

    for (ShoppingCartItem cartItem : cart.getItems().values())
    {
        Product product = cartItem.getProduct();
        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setOrderId(savedOrder.getOrderId());
        lineItem.setProduct(product);
        lineItem.setQuantity(cartItem.getQuantity());
        lineItem.setSalesPrice(product.getPrice());
        orderLineItemRepository.save(lineItem);
    }

    shoppingCartService.clearCart(userId);
    return savedOrder;
}
```

This code is important because checkout saves several related records in one workflow. The `@Transactional` annotation keeps order creation, line item creation, and cart clearing together as one database operation.

## Engineering Notes

- Controllers keep request handling small and delegate work to services.
- Services contain the main business logic.
- Repositories handle database access through Spring Data JPA.
- Protected endpoints use JWT authentication.
- Admin-only endpoints use `@PreAuthorize("hasRole('ROLE_ADMIN')")`.
- Cart and profile endpoints use the logged-in user from `Principal`, so users only work with their own data.
- Checkout is transactional because it creates an order, creates line items, and clears the cart as one workflow.

## Running Tests

Run all tests:

```powershell
.\mvnw.cmd test
```

Run a specific test class:

```powershell
.\mvnw.cmd "-Dtest=ProductServiceTest" test
```

## Notes

- The default API port is `8080`.
- The default database is `easyshop`.
- Protected endpoints require a JWT token.
- Admin endpoints require `ROLE_ADMIN`.
