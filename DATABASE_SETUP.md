# Configuración de Base de Datos MySQL/MariaDB
# Sistema Veterinario Garritas

## 📋 PASOS PARA CONECTAR LA BASE DE DATOS

### 1. **Preparar la Base de Datos**

Ejecuta estos comandos en MySQL/MariaDB:

```sql
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS garritas_veterinaria 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE garritas_veterinaria;

-- Ejecutar el esquema (desde database_schema_fixed.sql)
-- Ejecutar los datos iniciales (desde init_database.sql)
```

### 2. **Verificar Credenciales en application.properties**

Actualiza las credenciales en `src/main/resources/application.properties`:

```properties
# ACTUALIZAR ESTAS CREDENCIALES SEGÚN TU CONFIGURACIÓN
spring.datasource.url=jdbc:mysql://localhost:3306/garritas_veterinaria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_PASSWORD_MYSQL
```

### 3. **Usuarios de Prueba Creados**

| Usuario | Contraseña | Rol | Correo |
|---------|------------|-----|--------|
| admin | 123456 | Administrador | admin@garritas.com |
| veterinario | 123456 | Veterinario | maria@garritas.com |
| cliente1 | 123456 | Cliente | juan@email.com |
| cliente2 | 123456 | Cliente | ana@email.com |
| recepcion | 123456 | Recepcionista | sofia@garritas.com |

### 4. **Datos de Prueba Incluidos**

✅ **5 Roles** (Administrador, Veterinario, Cliente, Recepcionista)
✅ **5 Usuarios** con diferentes roles
✅ **6 Mascotas** (perros y gatos)
✅ **8 Servicios** veterinarios
✅ **10 Productos** en inventario
✅ **6 Citas** (programadas y completadas)
✅ **5 Historiales** clínicos
✅ **5 Reportes** de ejemplo

### 5. **Funcionalidades Web Habilitadas**

Ahora las páginas web mostrarán datos reales de la base de datos:

- **Dashboard**: Estadísticas en tiempo real
- **Usuarios**: Lista completa de usuarios registrados
- **Mascotas**: Todas las mascotas con sus propietarios
- **Citas**: Calendario con citas programadas y completadas
- **Inventario**: Productos con stock real
- **Reportes**: Reportes generados dinámicamente
- **Historial**: Historiales clínicos completos

### 6. **URLs del Sistema**

- **Aplicación Web**: http://localhost:8080
- **Login**: http://localhost:8080/login
- **Dashboard**: http://localhost:8080/dashboard
- **API REST**: http://localhost:8080/api/{endpoint}

### 7. **Verificación de Conexión**

El sistema incluye endpoints de health check:
- http://localhost:8080/health/health
- http://localhost:8080/health/database

## ⚡ INICIO RÁPIDO

1. Configura MySQL/MariaDB con los scripts SQL
2. Actualiza credenciales en application.properties
3. Ejecuta: `mvn spring-boot:run`
4. Navega a: http://localhost:8080
5. Inicia sesión con: admin / 123456

## 🔧 RESOLUCIÓN DE PROBLEMAS

**Error de conexión**: Verifica que MySQL esté ejecutándose y las credenciales sean correctas
**Tablas no existen**: Ejecuta primero database_schema_fixed.sql
**Sin datos**: Ejecuta init_database.sql
**Puerto ocupado**: Cambia server.port en application.properties

## 📊 INTERACCIÓN DATOS-PÁGINAS

Las páginas web ahora están conectadas a la base de datos:
- Datos dinámicos en todas las páginas
- Formularios funcionales
- Estadísticas en tiempo real
- Listados actualizados automáticamente