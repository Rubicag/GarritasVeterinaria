# Resumen de la migración a Java 21

Fecha: 2025-10-04

Este documento resume los cambios realizados para actualizar el proyecto a Java 21, cómo deshacerlos si es necesario y recomendaciones para CI y producción.

## Cambios aplicados
- Objetivo: migrar el proyecto WAR a compilación target Java 21.
- `pom.xml`:
  - Establecido `<maven.compiler.release>21</maven.compiler.release>`.
  - Actualizado `maven-compiler-plugin` a 3.11.0 con `<release>`.
  - Añadido `maven-enforcer-plugin` para requerir JDK >=21.
  - Añadidos plugins de QA: `jacoco-maven-plugin` y `maven-dependency-plugin` (configurado sin failOnWarning).
  - Ajustada `maven-surefire-plugin` para evitar problemas de module-path en JDK modernos.
- Maven Wrapper: `mvnw`, `mvnw.cmd` y `.mvn/wrapper/*` añadidos/ajustados para funcionar en Windows/Unix.
- Plantillas:
  - Sincronizadas inicialmente en `src/main/resources/templates` para pruebas.
  - Decidido mantener la fuente canónica en `src/main/webapp/WEB-INF/templates` (WAR clásico).
  - Eliminadas las duplicadas de `src/main/resources/templates`; respaldo en `target/backup-templates/`.
- Tests:
  - Añadidos tests básicos: `ExampleTest`, `HealthServiceTest`, `IntegrationTest` (placeholder) y `UsuarioServiceTest`.
  - `UsuarioService` recibió una implementación mínima (`countUsers()`), con su test correspondiente.
- CI:
  - Añadido workflow GitHub Actions en `.github/workflows/ci.yml` que usa Temurin JDK 21, ejecuta `./mvnw -B -U clean package`, genera reporte JaCoCo y sube artifacts (WAR y `target/site/jacoco`).

## Verificaciones realizadas
- Build local con wrapper: `./mvnw -U clean package` → BUILD SUCCESS.
- Tests: todos los tests añadidos pasaron localmente; resultados en `target/surefire-reports/`.
- WAR final generado: `target/GarritasVeterinaria-1.0-SNAPSHOT.war` y verificado que incluye `WEB-INF/templates/*.html`.
- JaCoCo: `target/jacoco.exec` y `target/site/jacoco` generados.

## Pasos para rollback (si es necesario)
1. Revertir cambios en el control de versiones (si usas Git):
   - `git checkout -- .` (si no has comiteado) o `git revert <commit>` para deshacer commits aplicados.
2. Restaurar plantillas desde la copia de seguridad si las borraste accidentalmente:
   - Copiar desde `target/backup-templates/` a `src/main/resources/templates/` o a `src/main/webapp/WEB-INF/templates/` según el estado previo.
3. Si hubo cambios en `pom.xml` que deseas revertir, usa el historial de Git para restaurar la versión anterior.

## Recomendaciones para CI y producción
- CI:
  - Mantener el Maven Wrapper en el repo para builds reproducibles.
  - Proteger secrets (tokens) usando GitHub Secrets si se publica el WAR a repositorios remotos.
  - Añadir un job opcional que publique el WAR a un repositorio de artefactos (Nexus/Artifactory) o cree una Release en GitHub (necesita token).
  - Configurar umbrales de cobertura JaCoCo si deseas que CI falle cuando la cobertura sea demasiado baja.
- Producción:
  - Asegurar que el runtime (servidor de aplicaciones o contenedor) ejecute una JVM compatible (JDK 21 o superior). Aunque compilado con `--release 21`, es compatible con JDK >=21.
  - Considerar empaquetar la app en un contenedor (Docker) con una base de runtime conocida (Temurin 21) para mayor reproducibilidad.
  - Hacer pruebas en staging con la misma JVM que en producción y realizar pruebas de humo antes del despliegue.

## Notas finales
- Si quieres que publique automáticamente el WAR en una Release de GitHub, puedo añadir el job al workflow; necesitarás un token con permisos `repo` (almacenado en GitHub Secrets).
- Si deseas tests más profundos para servicios (`UsuarioService`, `CitaService`, etc.), puedo añadir tests con Mockito y ejemplos de uso.

---
Migración completada localmente y pipeline básico agregado. Contacta si quieres que automatice la publicación del WAR o incremente la suite de tests.
# Upgrade to Java 21 (LTS)

This document describes steps to upgrade the project to Java 21 on Windows and how to verify and build the project.

Prerequisites
- Windows with administrator privileges for system-wide JDK installation (or use user install).
- Maven installed and on PATH.

1) Install JDK 21 (Adoptium / Eclipse Temurin)
- Manual download:
  1. Go to https://adoptium.net/temurin/releases/?version=21 and download the Windows x64 MSI or ZIP.
  2. Run the MSI as administrator and follow installer prompts.
- Using a package manager (scoop / choco):
  - With Chocolatey (admin PowerShell):

```powershell
choco install temurin21 -y
```

  - With Scoop (user PowerShell):

```powershell
scoop install temurin21
```

2) Configure JAVA_HOME and PATH (system or user environment variables)
- Set JAVA_HOME to the JDK21 installation folder (e.g. C:\Program Files\Eclipse Adoptium\jdk-21.0.x)
- Add `%JAVA_HOME%\bin` to the system PATH before other Java entries.

3) Verify installation

```powershell
java -version
javac -version
mvn -version
```

Expect output showing `openjdk version "21"` or similar.

4) Project changes already applied
- `pom.xml` has been updated to set `<maven.compiler.release>21</maven.compiler.release>` and `maven-compiler-plugin` to `3.11.0` using `<release>${maven.compiler.release}</release>`.

5) Build the project

```powershell
cd "c:\Users\LUIGGI\Documents\NetBeansProjects\SistemaWeb_Gestion_Ventas_Licoreria_DonPolo-1.0-SNAPSHOT\GarritasVeterinaria"
mvn -U clean package
```

If compilation errors appear, they may be caused by:
- APIs removed or changed in newer Java versions (rare for Jakarta EE-based code)
- Third-party libraries requiring updates

6) Common fixes
- Update third-party libs to Java 21 compatible versions.
- If `--release` prevents referencing newer APIs, consider using `<source>`/`<target>` and configure toolchains.

7) Optional: Configure Maven Toolchains plugin or `JAVA_HOME` in CI
- For reproducible builds, set `JAVA_HOME` in CI runner or use Maven Toolchains plugin to pin JDK 21.

8) Rollback plan
- Revert the `pom.xml` commit if the build fails and investigate incompatibilities.

---
If you'd like, I can:
- Try to detect installed JDKs from your machine (requires permission to run commands).
- Install Temurin 21 using the system installer (I can provide exact commands to run in admin PowerShell).
- Run a Maven build here if you enable terminal access.
