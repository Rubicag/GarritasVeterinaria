# Estructura recomendada para el proyecto GarritasVeterinaria

Este proyecto es una aplicación web empaquetada en WAR y gestionada con Maven.
A continuación se describe la estructura recomendada, carpetas nuevas sugeridas y por qué.

Estructura base (actual)

- pom.xml
- src/main/java/... (código Java)
- src/main/resources/... (application.properties, mensajes)
- src/main/webapp/WEB-INF/templates (vistas Thymeleaf/HTML)
- target/ (compilados)

Carpetas recomendadas a añadir

- src/test/java/
  - Código de pruebas unitarias e integración. Permite ejecutar `mvn test`.
- src/main/resources/static/
  - Contenido estático público (CSS, JS, imágenes). Aunque `webapp` contiene recursos web, usar `resources/static` facilita empaquetado y frameworks modernos.
- src/main/resources/templates/
  - Plantillas que se usan desde la aplicación (si tu antigua ubicación es `webapp/WEB-INF/templates` puedes migrarlas o mantener ambas; recomiendo consolidar en `src/main/webapp/WEB-INF/templates` para WAR clásico).
- .mvn/wrapper/
  - Carpeta para el Maven Wrapper (scripts binarios y jar del wrapper). No crearé scripts automáticamente aquí, pero tener la carpeta lista facilita añadir `mvnw` después.

Agregar Maven Wrapper (recomendado)

- Beneficio: otros desarrolladores no necesitan instalar Maven globalmente.
- Para generar wrapper necesitas una instalación de Maven temporal o local:
  mvn -N io.takari:maven:wrapper
  Esto crea `mvnw`, `mvnw.cmd` y la carpeta `.mvn/wrapper`.

Sugerencias para migración a Java 21

- Ya actualicé `pom.xml` para usar `release` 21.

- Añade tests en `src/test/java` y ejecutar `mvn -U test` en CI.

Notas finales

- Mantén `src/main/webapp` para recursos específicos de WAR (WEB-INF). Si quieres usar un enfoque más moderno (Spring Boot), podríamos reorganizar para que las plantillas y estáticos vivan en `src/main/resources`.

Si quieres, puedo:

- crear las carpetas vacías ahora (lo haré si me confirmas)
- añadir un `mvnw` + `.mvn/wrapper` al repo (necesito que me confirmes si quieres que lo genere: requiere tener mvn disponible en la sesión)
- mover plantillas estáticas a `src/main/resources` si prefieres modulizar la app

Dime qué quieres que cree automáticamente y lo hago: carpetas vacías, wrapper, o mover recursos.
