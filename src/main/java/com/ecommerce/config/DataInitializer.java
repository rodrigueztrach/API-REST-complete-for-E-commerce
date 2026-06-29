package com.ecommerce.config;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Inicializando datos de prueba...");

        // Create admin user
        User admin = User.builder()
                .firstName("Admin")
                .lastName("Sistema")
                .email("admin@ecommerce.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .build();
        admin = userRepository.save(admin);
        cartRepository.save(Cart.builder().user(admin).build());

        // Create customer user
        User customer = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@email.com")
                .password(passwordEncoder.encode("user123"))
                .phone("+506 8888-8888")
                .address("San José, Costa Rica")
                .role(User.Role.CUSTOMER)
                .build();
        customer = userRepository.save(customer);
        cartRepository.save(Cart.builder().user(customer).build());

        // Create categories
        Category electronics = categoryRepository.save(Category.builder()
                .name("Electrónica")
                .description("Dispositivos electrónicos y tecnología")
                .imageUrl("https://example.com/electronics.jpg")
                .build());

        Category clothing = categoryRepository.save(Category.builder()
                .name("Ropa")
                .description("Ropa y accesorios de moda")
                .imageUrl("https://example.com/clothing.jpg")
                .build());

        Category sports = categoryRepository.save(Category.builder()
                .name("Deportes")
                .description("Artículos deportivos y fitness")
                .imageUrl("https://example.com/sports.jpg")
                .build());

        // Create products
        List<Product> products = List.of(
            Product.builder()
                .name("iPhone 15 Pro")
                .description("El último iPhone con chip A17 Pro, cámara de 48MP y pantalla Super Retina XDR")
                .price(new BigDecimal("1299.99"))
                .stock(50)
                .sku("APPLE-IP15PRO")
                .category(electronics)
                .imageUrl("https://example.com/iphone15pro.jpg")
                .build(),
            Product.builder()
                .name("Samsung Galaxy S24")
                .description("Smartphone Android con Galaxy AI, cámara de 200MP y batería de 5000mAh")
                .price(new BigDecimal("999.99"))
                .stock(75)
                .sku("SAMSUNG-GS24")
                .category(electronics)
                .imageUrl("https://example.com/galaxys24.jpg")
                .build(),
            Product.builder()
                .name("MacBook Air M3")
                .description("Laptop ultradelgada con chip M3, 8GB RAM y 256GB SSD")
                .price(new BigDecimal("1099.00"))
                .stock(30)
                .sku("APPLE-MBA-M3")
                .category(electronics)
                .imageUrl("https://example.com/macbookair.jpg")
                .build(),
            Product.builder()
                .name("Camiseta Polo Clásica")
                .description("Camiseta polo 100% algodón piqué, disponible en múltiples colores")
                .price(new BigDecimal("29.99"))
                .stock(200)
                .sku("CLOTH-POLO-001")
                .category(clothing)
                .imageUrl("https://example.com/polo.jpg")
                .build(),
            Product.builder()
                .name("Jeans Slim Fit")
                .description("Pantalón jeans stretch slim fit, corte moderno y cómodo")
                .price(new BigDecimal("59.99"))
                .stock(150)
                .sku("CLOTH-JEANS-001")
                .category(clothing)
                .imageUrl("https://example.com/jeans.jpg")
                .build(),
            Product.builder()
                .name("Zapatillas Running Pro")
                .description("Zapatillas de running con amortiguación reactiva y suela de goma antideslizante")
                .price(new BigDecimal("89.99"))
                .stock(100)
                .sku("SPORT-RUN-001")
                .category(sports)
                .imageUrl("https://example.com/zapatillas.jpg")
                .build(),
            Product.builder()
                .name("Pesas Ajustables 20kg")
                .description("Set de pesas ajustables de 2kg a 20kg, incluye barra y collares de seguridad")
                .price(new BigDecimal("149.99"))
                .stock(40)
                .sku("SPORT-PESAS-020")
                .category(sports)
                .imageUrl("https://example.com/pesas.jpg")
                .build(),
            Product.builder()
                .name("Auriculares Sony WH-1000XM5")
                .description("Auriculares inalámbricos con cancelación de ruido líder en la industria")
                .price(new BigDecimal("349.99"))
                .stock(60)
                .sku("SONY-WH1000XM5")
                .category(electronics)
                .imageUrl("https://example.com/sony-wh.jpg")
                .build()
        );

        productRepository.saveAll(products);

        log.info(" Datos inicializados correctamente");
        log.info(" Usuarios creados:");
        log.info("   Admin: admin@ecommerce.com / admin123");
        log.info("   Cliente: juan@email.com / user123");
        log.info(" {} productos creados en {} categorías", products.size(), 3);
        log.info(" Swagger UI: http://localhost:8080/swagger-ui.html");
        log.info("  H2 Console: http://localhost:8080/h2-console");
    }
}
