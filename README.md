# GarritasVeterinaria

Proyecto Java (Jakarta EE) empaquetado como WAR. Se ha actualizado para compilar con Java 21.

Instrucciones rápidas

1. Usar el wrapper (recomendado):

```markdown
# GarritasVeterinaria

Proyecto Java (Jakarta EE) empaquetado como WAR. Se ha actualizado para compilar con Java 21.

Requisitos locales
- Java JDK 21+ instalado (se recomienda Temurin/Adoptium). Si usas la versión del sistema (p. ej. JDK 22) está bien mientras compile para <release>21.
- No es obligatorio tener Maven instalado: usa el Maven Wrapper incluido (`mvnw` / `mvnw.cmd`).

Comandos útiles

- Construir el paquete (Windows PowerShell):

```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\mvnw.cmd -U clean package
```

- Construir en Unix/macOS:

```bash
./mvnw -U clean package
```

- Ejecutar tests (JUnit 5):

```powershell
.\mvnw.cmd test
```

- Generar informe de cobertura (JaCoCo) y ver HTML localmente:

```powershell
.\mvnw.cmd -DskipTests=false clean package
# luego abrir target/site/jacoco/index.html
```

Estructura de plantillas
- Las plantillas se mantienen en `src/main/webapp/WEB-INF/templates` (ruta empaquetada en el WAR). No existen duplicados en `src/main/resources/templates`.

Integración continua (CI)
- El proyecto incluye un workflow de GitHub Actions que usa Java 21 y el Maven Wrapper para construir, ejecutar tests y publicar artefactos (WAR) y reportes de cobertura.

Consejos rápidos
- Si CI falla por falta de memoria en la JVM en su runner, aumenta `MAVEN_OPTS` o ajusta `argLine` en `pom.xml`.
- Para inspeccionar dependencias: `./mvnw dependency:tree`.

```
