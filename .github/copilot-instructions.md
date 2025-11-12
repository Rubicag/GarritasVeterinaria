## Propósito rápido

Guía breve para agentes (Copilot/AI) que trabajan en este repo Java/Spring Boot empaquetado como WAR.
Objetivo: dar contexto inmediato, comandos útiles y patrones de código específicos del proyecto.

--

## Visión general del proyecto

- Aplicación Spring Boot (clase principal: `com.mycompany.VeterinariaApplication`).
- Empaquetado: WAR (ver `pom.xml`, plugin `spring-boot-maven-plugin` con `mainClass` configurada).
- Principales paquetes: `controller`, `service`, `repository`, `model`, `security`, `config`.
- Plantillas Thymeleaf están en `src/main/webapp/WEB-INF/templates` (no en `resources/templates`).

## Flujo de datos y límites de servicio

- Controladores REST exponen rutas bajo `/api/*` (ej.: `com.mycompany.controller.UsuarioController` → `/api/usuarios`).
- Lógica de negocio en `service` (ej.: `UsuarioService` usa `UsuarioRepository` — Spring Data JPA).
- Persistencia: Spring Data JPA + H2 para desarrollo/pruebas, MySQL para producción (`application-h2.properties`, `application-mysql.properties`).
- **Seguridad dual condicional**:
  - JWT activado (default): `WebSecurityConfig` con `@ConditionalOnProperty(jwt.enabled=true)` maneja autenticación stateless + filtro JWT.
  - JWT desactivado: `SecurityConfig` con `@ConditionalOnProperty(jwt.enabled=false)` proporciona autenticación básica/form login.
  - **Importante**: evitar duplicar beans `authenticationManager` o `SecurityFilterChain` — usar nombres distintos y condiciones mutuamente excluyentes.

## Comandos y flujos de desarrollo (concrete)

- Compilar (Windows PowerShell, usa mvnw):

  .\mvnw.cmd -U clean package

- Ejecutar app local (perfil H2):

  .\mvnw.cmd -Dspring-boot.run.profiles=h2 spring-boot:run

  o alternativamente

  .\mvnw.cmd -Dspring.profiles.active=h2 spring-boot:run

- Ejecutar pruebas (perfil H2):

  .\mvnw.cmd test -Dspring.profiles.active=h2

  Ejecutar prueba concreta:

  .\mvnw.cmd test -Dtest=UsuarioServiceTest

- Cobertura JaCoCo (HTML): ejecutar `clean package` y abrir `target/site/jacoco/index.html`.

## Perfiles y configuración relevantes

- `default` → producción (MySQL).  `h2` → DB en memoria usada en desarrollo y tests.
- Propiedad útil para pruebas/deshabilitar JWT: `jwt.enabled=false` o `app.security.jwt.enabled=false` (usada en tests/README).

## Convenciones de código y patrones a respetar

- Repositorios: extienden `JpaRepository` (ver `com.mycompany.repository.UsuarioRepository`).
- Servicios: anotados con `@Service` y `@Transactional` (ej.: `UsuarioService`), siguen patrón CRUD explícito.
- Controladores REST: usan `@RestController` y devuelven `ResponseEntity` para estados HTTP.
- Plantillas Web (Thymeleaf) y controladores MVC separadas de los controladores API (ver `WebController`, `CitasWebController`).

## Puntos de integración y herramientas auxiliares

- Herramientas de línea de comandos incluidas: `com.mycompany.tools.DbImportTool` y `ConnectionTester` (clases con `main`) — úsalas para importar/validar datos.
- Migraciones de esquema: gestionadas por JPA/Hibernate (`spring.jpa.hibernate.ddl-auto` en `application-*.properties`).

## Donde mirar primero (archivos de ejemplo)

- `README.md` – overview y comandos.
- `pom.xml` – dependencias, plugins, packaging (WAR) y mainClass.
- `src/main/java/com/mycompany/VeterinariaApplication.java` – entrypoint.
- `src/main/resources/application-h2.properties` – configuración H2 usada en tests/desarrollo.
- `src/main/java/com/mycompany/controller/UsuarioController.java` – ejemplo REST CRUD.
- `src/main/java/com/mycompany/service/UsuarioService.java` – patrón servicio y uso de PasswordEncoder.
- `src/main/java/com/mycompany/repository/UsuarioRepository.java` – ejemplo Spring Data JPA.

## Ejemplos rápidos que un agente puede generar o cambiar con confianza

- Añadir un endpoint CRUD simple: seguir `UsuarioController` y `UsuarioService` como plantilla.
- Crear pruebas con perfil `h2`: usar `@ActiveProfiles("h2")` o ejecutar `mvn test -Dspring.profiles.active=h2`.

## Errores y límites conocidos (descubribles)

- Proyecto empaquetado como WAR → algunos despliegues esperan servlet container externo. Para pruebas locales usa `spring-boot:run` o `java -jar` del repackage si el runner lo admite.
- Si CI falla por memoria, ajustar `MAVEN_OPTS` o `argLine` en `pom.xml` (ver `maven-surefire-plugin` configuración).

## Resumen rápido (contrato)

- Inputs: cambios en `src/main/java`, `src/main/resources`, templates en `src/main/webapp/WEB-INF/templates`.
- Outputs esperados: WAR en `target/`, reportes de test en `target/surefire-reports`, informe JaCoCo en `target/site/jacoco`.
- Éxito: build `mvnw clean package` pasa sin tests fallidos; endpoints API responden bajo `/api/*`.

---

Si quieres, puedo: 1) ajustar el tono/longitud; 2) añadir ejemplos de endpoints adicionales (ej.: `/api/citas`); 3) incluir snippets de configuración de `application.properties` para CI. ¿Qué prefieres que detalle más? 
