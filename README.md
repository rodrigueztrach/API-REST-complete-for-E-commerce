# 🛒 E-Commerce REST API — Spring Boot

API REST completa para e-commerce con **Spring Boot 3**, **Spring Security + JWT**, **JPA/Hibernate** y documentación **Swagger/OpenAPI**.

---

## 🚀 Inicio Rápido

```bash
# Clonar / abrir el proyecto
cd ecommerce-api

# Compilar y ejecutar
./mvnw spring-boot:run
```

El servidor inicia en `http://localhost:8080`

---

## 📋 Usuarios de Prueba (precargados)

| Rol      | Email                  | Contraseña |
|----------|------------------------|------------|
| 👑 Admin  | admin@ecommerce.com    | admin123   |
| 👤 Cliente | juan@email.com        | user123    |

---

## 🔗 URLs Importantes

| Servicio        | URL                                        |
|-----------------|--------------------------------------------|
| Swagger UI      | http://localhost:8080/swagger-ui.html      |
| API Docs (JSON) | http://localhost:8080/api-docs             |
| H2 Console      | http://localhost:8080/h2-console           |

**H2 Console config:**
- JDBC URL: `jdbc:h2:mem:ecommercedb`
- Usuario: `sa`  
- Contraseña: `password`

---

## 📚 Endpoints de la API

### 🔐 Autenticación (`/api/auth`)
| Método | Endpoint             | Descripción         | Auth |
|--------|----------------------|---------------------|------|
| POST   | `/api/auth/register` | Registrar usuario   |    |
| POST   | `/api/auth/login`    | Iniciar sesión      |    |

### 📦 Productos (`/api/products`)
| Método | Endpoint                          | Descripción                  | Auth    |
|--------|-----------------------------------|------------------------------|---------|
| GET    | `/api/products`                   | Listar todos (paginado)      |       |
| GET    | `/api/products/{id}`              | Obtener por ID               |       |
| GET    | `/api/products/search?keyword=`   | Buscar por nombre/descripción|      |
| GET    | `/api/products/category/{id}`     | Filtrar por categoría        |      |
| GET    | `/api/products/price-range`       | Filtrar por precio           |       |
| POST   | `/api/products`                   | Crear producto               |  Admin|
| PUT    | `/api/products/{id}`              | Actualizar producto          |  Admin|
| DELETE | `/api/products/{id}`              | Eliminar producto (soft)     |  Admin|
| PATCH  | `/api/products/{id}/stock`        | Ajustar stock                |  Admin|

### Categorías (`/api/categories`)
| Método | Endpoint                  | Descripción          | Auth    |
|--------|---------------------------|----------------------|---------|
| GET    | `/api/categories`         | Listar categorías    |       |
| GET    | `/api/categories/{id}`    | Obtener por ID       |       |
| POST   | `/api/categories`         | Crear categoría      |  Admin|
| PUT    | `/api/categories/{id}`    | Actualizar categoría |  Admin|
| DELETE | `/api/categories/{id}`    | Eliminar categoría   |  Admin|

###  Carrito (`/api/cart`)
| Método | Endpoint                   | Descripción             | Auth      |
|--------|----------------------------|-------------------------|-----------|
| GET    | `/api/cart`                | Ver mi carrito          | 🔑 JWT    |
| POST   | `/api/cart/items`          | Agregar producto        | 🔑 JWT    |
| PUT    | `/api/cart/items/{itemId}` | Cambiar cantidad        | 🔑 JWT    |
| DELETE | `/api/cart/items/{itemId}` | Eliminar item           | 🔑 JWT    |
| DELETE | `/api/cart`                | Vaciar carrito          | 🔑 JWT    |

###  Usuarios (`/api/users`)
| Método | Endpoint              | Descripción              | Auth      |
|--------|-----------------------|--------------------------|-----------|
| GET    | `/api/users/me`       | Ver mi perfil            |  JWT    |
| PUT    | `/api/users/me`       | Actualizar perfil        |  JWT    |
| PATCH  | `/api/users/me/password` | Cambiar contraseña    |  JWT    |
| GET    | `/api/users`          | Listar todos usuarios    |  Admin  |
| GET    | `/api/users/{id}`     | Ver usuario por ID       |  Admin  |
| DELETE | `/api/users/{id}`     | Desactivar usuario       |  Admin  |

---

## 📝 Ejemplos de Uso (cURL)

### 1. Registrar usuario
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

### 2. Iniciar sesión y obtener token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@email.com",
    "password": "user123"
  }'
```

### 3. Buscar productos
```bash
curl "http://localhost:8080/api/products/search?keyword=samsung&page=0&size=5"
```

### 4. Agregar al carrito (con token JWT)
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1, "quantity": 2 }'
```

### 5. Ver el carrito
```bash
curl http://localhost:8080/api/cart \
  -H "Authorization: Bearer {TOKEN}"
```

### 6. Crear producto (Admin)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Producto",
    "description": "Descripción del producto",
    "price": 49.99,
    "stock": 100,
    "sku": "PROD-001",
    "categoryId": 1
  }'
```

---

##  Arquitectura del Proyecto

```
src/main/java/com/ecommerce/
├── config/
│   ├── DataInitializer.java      # Datos de prueba precargados
│   ├── OpenApiConfig.java        # Configuración Swagger
│   └── SecurityConfig.java       # Spring Security + JWT
├── controller/
│   ├── AuthController.java       # POST /register, /login
│   ├── ProductController.java    # CRUD productos + búsqueda
│   ├── CategoryController.java   # CRUD categorías
│   ├── CartController.java       # Gestión del carrito
│   └── UserController.java       # Perfil de usuario
├── dto/
│   ├── ApiResponse.java          # Wrapper genérico de respuesta
│   ├── AuthDto.java              # DTOs de autenticación
│   ├── CartDto.java              # DTOs del carrito
│   ├── CategoryDto.java          # DTOs de categorías
│   ├── ProductDto.java           # DTOs de productos
│   └── UserDto.java              # DTOs de usuarios
├── entity/
│   ├── User.java                 # Entidad usuario (roles CUSTOMER/ADMIN)
│   ├── Product.java              # Entidad producto
│   ├── Category.java             # Entidad categoría
│   ├── Cart.java                 # Carrito (1:1 con User)
│   └── CartItem.java             # Items del carrito
├── exception/
│   ├── GlobalExceptionHandler.java # Manejo centralizado de errores
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── ConflictException.java
├── repository/                   # Spring Data JPA Repositories
├── security/
│   ├── JwtUtil.java              # Generación/validación JWT
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

## 🔒 Seguridad

- **JWT (JSON Web Tokens)** para autenticación stateless
- **BCrypt** para hash de contraseñas
- Roles: `CUSTOMER` y `ADMIN`
- Endpoints públicos: catálogo de productos, categorías, auth
- Endpoints protegidos: carrito, perfil de usuario
- Endpoints de admin: CRUD de productos/categorías, gestión de usuarios

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Security | 6.x | Autenticación/Autorización |
| Spring Data JPA | 3.x | Persistencia de datos |
| H2 Database | - | BD en memoria (desarrollo) |
| JWT (jjwt) | 0.11.5 | Tokens de seguridad |
| Lombok | - | Reducción de boilerplate |
| SpringDoc OpenAPI | 2.3.0 | Documentación Swagger |

---

## ⚙️ Configuración para Producción

Para producción, reemplazar H2 por PostgreSQL/MySQL en `application.properties`:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommercedb
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate

# JWT (usar variable de entorno)
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```
