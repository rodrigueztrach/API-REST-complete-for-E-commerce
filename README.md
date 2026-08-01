# E-Commerce REST API — Spring Boot

Complete REST API for e-commerce built with **Spring Boot 3**, **Spring Security + JWT**, **JPA/Hibernate**, and **Swagger/OpenAPI** documentation.

---

## Quick Start

```bash
# Clone / open the project
cd ecommerce-api

# Build and run
./mvnw spring-boot:run
```

The server starts at `http://localhost:8080`


## Important URLs

| Service         | URL                                        |
|------------------|--------------------------------------------|
| Swagger UI       | http://localhost:8080/swagger-ui.html      |
| API Docs (JSON)  | http://localhost:8080/api-docs             |
| H2 Console       | http://localhost:8080/h2-console           |


## API Endpoints

### Authentication (`/api/auth`)
| Method | Endpoint             | Description         | Auth |
|--------|-----------------------|---------------------|------|
| POST   | `/api/auth/register` | Register user       |  No  |
| POST   | `/api/auth/login`    | Log in              |  No  |

### Products (`/api/products`)
| Method | Endpoint                          | Description                    | Auth    |
|--------|-----------------------------------|---------------------------------|---------|
| GET    | `/api/products`                   | List all (paginated)           |  No     |
| GET    | `/api/products/{id}`              | Get by ID                      |  No     |
| GET    | `/api/products/search?keyword=`   | Search by name/description      |  No     |
| GET    | `/api/products/category/{id}`     | Filter by category             |  No     |
| GET    | `/api/products/price-range`       | Filter by price                |  No     |
| POST   | `/api/products`                   | Create product                 |  Admin  |
| PUT    | `/api/products/{id}`              | Update product                 |  Admin  |
| DELETE | `/api/products/{id}`              | Delete product (soft delete)   |  Admin  |
| PATCH  | `/api/products/{id}/stock`        | Adjust stock                   |  Admin  |

### Categories (`/api/categories`)
| Method | Endpoint                  | Description           | Auth    |
|--------|---------------------------|-----------------------|---------|
| GET    | `/api/categories`         | List categories       |  No     |
| GET    | `/api/categories/{id}`    | Get by ID              |  No     |
| POST   | `/api/categories`         | Create category        |  Admin  |
| PUT    | `/api/categories/{id}`    | Update category        |  Admin  |
| DELETE | `/api/categories/{id}`    | Delete category        |  Admin  |

### Cart (`/api/cart`)
| Method | Endpoint                   | Description               | Auth |
|--------|-----------------------------|---------------------------|------|
| GET    | `/api/cart`                | View my cart               | JWT  |
| POST   | `/api/cart/items`          | Add product                | JWT  |
| PUT    | `/api/cart/items/{itemId}` | Change quantity             | JWT  |
| DELETE | `/api/cart/items/{itemId}` | Remove item                | JWT  |
| DELETE | `/api/cart`                | Empty cart                  | JWT  |

### Users (`/api/users`)
| Method | Endpoint                 | Description              | Auth  |
|--------|---------------------------|--------------------------|-------|
| GET    | `/api/users/me`          | View my profile           | JWT   |
| PUT    | `/api/users/me`          | Update profile             | JWT   |
| PATCH  | `/api/users/me/password` | Change password            | JWT   |
| GET    | `/api/users`             | List all users             | Admin |
| GET    | `/api/users/{id}`        | View user by ID            | Admin |
| DELETE | `/api/users/{id}`        | Deactivate user             | Admin |

---

## 📝 Usage Examples (cURL)

### 1. Register a user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "María",
    "lastName": "García",
    "email": "maria@email.com",
    "password": "password123",
    "phone": "+506 7777-7777",
    "address": "Heredia, Costa Rica"
  }'
```

### 2. Log in and get a token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@email.com",
    "password": "user123"
  }'
```

### 3. Search products
```bash
curl "http://localhost:8080/api/products/search?keyword=samsung&page=0&size=5"
```

### 4. Add to cart (with JWT token)
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1, "quantity": 2 }'
```

### 5. View the cart
```bash
curl http://localhost:8080/api/cart \
  -H "Authorization: Bearer {TOKEN}"
```

### 6. Create a product (Admin)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Product",
    "description": "Product description",
    "price": 49.99,
    "stock": 100,
    "sku": "PROD-001",
    "categoryId": 1
  }'
```

---

## Project Architecture

```
src/main/java/com/ecommerce/
├── config/
│   ├── DataInitializer.java      # Preloaded test data
│   ├── OpenApiConfig.java        # Swagger configuration
│   └── SecurityConfig.java       # Spring Security + JWT
├── controller/
│   ├── AuthController.java       # POST /register, /login
│   ├── ProductController.java    # Product CRUD + search
│   ├── CategoryController.java   # Category CRUD
│   ├── CartController.java       # Cart management
│   └── UserController.java       # User profile
├── dto/
│   ├── ApiResponse.java          # Generic response wrapper
│   ├── AuthDto.java              # Authentication DTOs
│   ├── CartDto.java              # Cart DTOs
│   ├── CategoryDto.java          # Category DTOs
│   ├── ProductDto.java           # Product DTOs
│   └── UserDto.java              # User DTOs
├── entity/
│   ├── User.java                 # User entity (CUSTOMER/ADMIN roles)
│   ├── Product.java              # Product entity
│   ├── Category.java             # Category entity
│   ├── Cart.java                 # Cart (1:1 with User)
│   └── CartItem.java             # Cart items
├── exception/
│   ├── GlobalExceptionHandler.java # Centralized error handling
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── ConflictException.java
├── repository/                   # Spring Data JPA Repositories
├── security/
│   ├── JwtUtil.java              # JWT generation/validation
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
└── service/
    ├── AuthService.java + impl/
    ├── ProductService.java + impl/
    ├── CategoryService.java + impl/
    ├── CartService.java + impl/
    └── UserService.java + impl/
```

---

## Security

- **JWT (JSON Web Tokens)** for stateless authentication
- **BCrypt** for password hashing
- Roles: `CUSTOMER` and `ADMIN`
- Public endpoints: product catalog, categories, auth
- Protected endpoints: cart, user profile
- Admin endpoints: product/category CRUD, user management

---

## 🛠️ Technologies

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 3.2.0 | Main framework |
| Spring Security | 6.x | Authentication/Authorization |
| Spring Data JPA | 3.x | Data persistence |
| H2 Database | - | In-memory DB (development) |
| JWT (jjwt) | 0.11.5 | Security tokens |
| Lombok | - | Boilerplate reduction |
| SpringDoc OpenAPI | 2.3.0 | Swagger documentation |

---

## ⚙️ Production Configuration

For production, replace H2 with PostgreSQL/MySQL in `application.properties`:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommercedb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate

# JWT (use environment variable)
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```
# API-REST-complete-for-E-commerce
