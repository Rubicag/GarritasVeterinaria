# 🗄️ Guía de Base de Datos - GarritasVeterinaria

## 📊 **Configuraciones Disponibles**

### 🔧 **Desarrollo (H2 Database)**

```bash
# Ejecutar con H2 (base de datos en memoria)
.\mvnw spring-boot:run

# O explícitamente:
.\mvnw spring-boot:run -Dspring-boot.run.profiles=default
```

**URLs de desarrollo:**

- **Aplicación**: <http://localhost:8080>
- **H2 Console**: <http://localhost:8080/h2-console>
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: (vacía)

### 🚀 **Producción (MySQL/MariaDB)**

```bash
# Ejecutar con MySQL
.\mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# O configurar variable de entorno:
set SPRING_PROFILES_ACTIVE=mysql
.\mvnw spring-boot:run
```

**URLs de producción:**

- **Aplicación**: <http://localhost:8081>
- **Base de datos**: localhost:3306/garritas_veterinaria

## 👥 **Credenciales de Usuario**

### **🔐 Usuarios Disponibles:**

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `admin` | ADMIN | Administrador del sistema |
| `jperez` | `secret` | CLIENTE | Juan Pérez (Cliente) |
| `mlopez` | `vetpass` | VETERINARIO | Dra. María López |

### **🐕 Mascotas de Ejemplo:**

| Nombre | Especie | Raza | Propietario |
|--------|---------|------|-------------|
| Firulais | Canino | Criollo | Juan Pérez |
| Michi | Felino | Siamés | Juan Pérez |
| Luna | Felino | Siam | Admin |

## 🛠️ **Comandos de Base de Datos**

### **📥 Importar datos MySQL:**

```bash
# Si quieres importar tu dump completo:
mysql -u root -p garritas_veterinaria < path/to/your/dump.sql
```

### **🔄 Sincronizar datos:**

```bash
# Para actualizar H2 con los datos de MySQL:
# 1. Ejecutar con perfil MySQL para obtener datos actuales
# 2. Exportar datos necesarios
# 3. Actualizar data.sql según sea necesario
```

## 🚀 **Configuración Automática**

### **H2 (Desarrollo):**

- ✅ Creación automática de tablas
- ✅ Datos de ejemplo cargados automáticamente
- ✅ Reinicio limpio en cada ejecución
- ✅ Ideal para desarrollo y testing

### **MySQL (Producción):**

- ✅ Conexión a base de datos real
- ✅ Datos persistentes
- ✅ Validación de esquema existente
- ✅ Pool de conexiones configurado

## 📋 **Comandos de Verificación**

### **Verificar conexión H2:**

```bash
# Arrancar aplicación
.\mvnw spring-boot:run

# Verificar en: http://localhost:8080/h2-console
```

### **Verificar conexión MySQL:**

```bash
# Arrancar con MySQL
.\mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# Verificar logs de conexión en consola
```

## 🔧 **Resolución de Problemas**

### **Error de conexión MySQL:**

1. Verificar que MariaDB/MySQL esté ejecutándose
2. Confirmar usuario `root` sin contraseña
3. Verificar que la BD `garritas_veterinaria` exista

### **Error en H2:**

1. Verificar que no hay conflictos de puerto
2. Limpiar cache: `.\mvnw clean`
3. Reiniciar aplicación

## 📊 **Estructura de Datos**

**Tablas disponibles:**

- `rol` - Roles del sistema
- `usuario` - Usuarios del sistema  
- `mascota` - Mascotas registradas
- `servicio` - Servicios veterinarios
- `producto` - Productos e inventario
- `cita` - Citas programadas
- `historialclinico` - Historiales médicos
- `reporte` - Reportes del sistema

---

**✅ Base de datos completamente configurada y lista para usar** 🎯
