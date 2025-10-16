# Solución de Errores ApplicationContext - Spring Boot

## 🔍 **Problema Identificado**

**Error Principal**: ApplicationContext failure threshold exceeded (5) - Tests fallaban al cargar el contexto de Spring Boot.

### 1. **Configuración JWT Conflictiva**

- La clase `WebSecurityConfig` intentaba inyectar `JwtFilter` y `JwtUtil`
- Estos beans no estaban disponibles en el contexto de tests
- Causaba errores de inyección de dependencias

### 2. **Prioridad de Configuraciones**

- `TestSecurityConfig` no tenía suficiente prioridad sobre `WebSecurityConfig`
- La configuración de producción se cargaba antes que la de tests

## 🛠️ **Soluciones Implementadas**

### 1. **Configuración de Propiedades Mejorada**

**Archivo**: `src/test/resources/application-h2.properties`

```properties
# JWT Configuration - DISABLED for tests
jwt.enabled=false
jwt.secret=test-secret-key-for-testing-only
jwt.expiration=3600000

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration optimized for tests
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging Configuration - Reduced for tests
logging.level.org.springframework.security=WARN
logging.level.org.hibernate=WARN
logging.level.com.mycompany=INFO
```

### 2. **TestSecurityConfig Reforzado**

**Archivo**: `src/test/java/com/mycompany/config/TestSecurityConfig.java`

```java
@TestConfiguration
@EnableWebSecurity
@Primary
@Order(1)
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    @Primary 
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder(4); // Reduced complexity for tests
    }
}
```

### 3. **Configuración Anti-JWT**

**Archivo**: `src/test/java/com/mycompany/config/NoJwtConfig.java`

- Configuración explícita para cuando JWT está deshabilitado
- Previene la carga de beans JWT en tests

### 4. **Test Básico de Contexto**

**Archivo**: `src/test/java/com/mycompany/garritasveterinaria/BasicContextTest.java`

```java
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "jwt.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@TestPropertySource(locations = "classpath:application-h2.properties")
@ActiveProfiles("test")
class BasicContextTest {

    @Test
    void contextLoads() {
        // Test passes if Spring context loads successfully
        assertThat(true).isTrue();
    }
}
```

## 📊 **Resultados**

### ✅ **Antes vs Después**

| Aspecto | Antes | Después |
|---------|-------|---------|
| **ApplicationContext** | 5 fallos consecutivos | Carga exitosamente |
| **JWT en Tests** | Conflictos de beans | Deshabilitado explícitamente |
| **Security Config** | Conflictos de prioridad | `@Primary` y `@Order(1)` |
| **Tests** | Fallaban al inicio | Ejecutan correctamente |

### 🎯 **Tests Funcionando**

1. ✅ `BasicContextTest` - Test de contexto básico
2. ✅ Configuración de seguridad para tests
3. ✅ Base de datos H2 en memoria
4. ✅ Propiedades específicas de test

### 🚀 **Comandos de Verificación**

```bash
# Test específico de contexto
.\mvnw test -Dtest=BasicContextTest

# Todos los tests
.\mvnw test

# Con información detallada
.\mvnw test -X

# Solo compilación
.\mvnw clean compile
```

## 📝 **Resumen Técnico**

### **Problema Principal**

ApplicationContext fallaba por dependencias JWT no resueltas en entorno de tests.

### **Solución Aplicada**

1. **Múltiples niveles de desactivación**: Propiedades + configuración explícita + exclusión de autoconfiguration
2. **Prioridad de configuración**: `@Primary` + `@Order(1)` para TestSecurityConfig
3. **Configuración específica**: Properties dedicadas para tests con H2
4. **Test de verificación**: BasicContextTest para validar carga de contexto

### **Archivos Modificados/Creados**

- ✅ `src/test/resources/application-h2.properties` - Mejorado
- ✅ `src/test/java/com/mycompany/config/TestSecurityConfig.java` - Reforzado
- ✅ `src/test/java/com/mycompany/config/NoJwtConfig.java` - Nuevo
- ✅ `src/test/java/com/mycompany/garritasveterinaria/BasicContextTest.java` - Nuevo

---

**Estado**: ✅ **PROBLEMA COMPLETAMENTE RESUELTO**  
**Fecha**: 11 de octubre de 2025
