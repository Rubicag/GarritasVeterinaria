# GarritasVeterinaria# GarritasVeterinaria



Proyecto Spring Boot para gestión de una clínica veterinaria. Actualizado para compilar con Java 21.Proyecto Java (Jakarta EE) empaquetado como WAR. Se ha actualizado para compilar con Java 21.



## RequisitosInstrucciones rápidas



- Java JDK 21+ instalado (se recomienda Temurin/Adoptium)1. Usar el wrapper (recomendado):

- No es obligatorio tener Maven instalado: usa el Maven Wrapper incluido (`mvnw` / `mvnw.cmd`)

- MySQL para producción, H2 para pruebas```markdown

# GarritasVeterinaria

## Comandos útiles

Proyecto Java (Jakarta EE) empaquetado como WAR. Se ha actualizado para compilar con Java 21.

### Construir el proyecto

Requisitos locales

- Windows PowerShell:- Java JDK 21+ instalado (se recomienda Temurin/Adoptium). Si usas la versión del sistema (p. ej. JDK 22) está bien mientras compile para <release>21.

- No es obligatorio tener Maven instalado: usa el Maven Wrapper incluido (`mvnw` / `mvnw.cmd`).

```powershell

Set-ExecutionPolicy -Scope Process -ExecutionPolicy BypassComandos útiles

.\mvnw.cmd -U clean package

```- Construir el paquete (Windows PowerShell):



- Unix/macOS:```powershell

Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

```bash.\mvnw.cmd -U clean package

./mvnw -U clean package```

```

- Construir en Unix/macOS:

### Ejecutar la aplicación

```bash

```powershell./mvnw -U clean package

.\mvnw.cmd spring-boot:run```

```

- Ejecutar tests (JUnit 5):

### Ejecutar pruebas

```powershell

```powershell.\mvnw.cmd test

# Ejecutar todas las pruebas```

.\mvnw.cmd test

- Generar informe de cobertura (JaCoCo) y ver HTML localmente:

# Ejecutar pruebas con perfil específico

.\mvnw.cmd test -Dspring.profiles.active=h2```powershell

.\mvnw.cmd test -Dspring.profiles.active=test.\mvnw.cmd -DskipTests=false clean package

# luego abrir target/site/jacoco/index.html

# Ejecutar una clase de prueba específica```

.\mvnw.cmd test -Dtest=UsuarioServiceTest

```Estructura de plantillas

- Las plantillas se mantienen en `src/main/webapp/WEB-INF/templates` (ruta empaquetada en el WAR). No existen duplicados en `src/main/resources/templates`.

### Generar informe de cobertura (JaCoCo)

Integración continua (CI)

```powershell- El proyecto incluye un workflow de GitHub Actions que usa Java 21 y el Maven Wrapper para construir, ejecutar tests y publicar artefactos (WAR) y reportes de cobertura.

.\mvnw.cmd -DskipTests=false clean package

# Luego abrir target/site/jacoco/index.htmlConsejos rápidos

```- Si CI falla por falta de memoria en la JVM en su runner, aumenta `MAVEN_OPTS` o ajusta `argLine` en `pom.xml`.

- Para inspeccionar dependencias: `./mvnw dependency:tree`.

## Perfiles de configuración

```

- `default`: Configuración de producción con MySQL
- `h2`: Base de datos en memoria H2 para desarrollo rápido
- `test`: Base de datos H2 con configuración específica para pruebas

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/mycompany/
│   │       ├── config/         # Configuración de Spring Boot
│   │       ├── controller/     # Controladores REST y Web
│   │       ├── model/          # Entidades JPA
│   │       ├── repository/     # Repositorios Spring Data JPA
│   │       ├── security/       # Configuración de seguridad JWT
│   │       ├── service/        # Servicios de negocio
│   │       └── VeterinariaApplication.java  # Clase principal
│   └── resources/
│       ├── application.properties           # Configuración principal
│       ├── application-h2.properties        # Configuración para H2
│       └── templates/                       # Plantillas Thymeleaf
└── test/
    ├── java/
    │   └── com/mycompany/
    │       ├── config/         # Configuración para pruebas
    │       ├── controller/     # Pruebas de controladores
    │       └── service/        # Pruebas de servicios
    └── resources/
        ├── application-h2.properties        # Configuración H2 para pruebas
        ├── application-test.properties      # Configuración específica para pruebas
        └── data-test.sql                    # Datos de prueba
```

## Seguridad

El sistema utiliza seguridad basada en JWT (JSON Web Token) con roles:

- `ADMIN`: Acceso completo al sistema
- `VETERINARIO`: Acceso a funciones médicas y citas
- `USER`: Acceso limitado a información personal y mascotas

Para pruebas, la seguridad JWT se puede deshabilitar con la propiedad `jwt.enabled=false` o `app.security.jwt.enabled=false`.

## Base de datos

- Producción: MySQL
- Pruebas/Desarrollo: H2 (en memoria)

Las migraciones de esquema se gestionan mediante JPA/Hibernate.