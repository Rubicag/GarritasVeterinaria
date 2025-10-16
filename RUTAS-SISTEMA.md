# 📋 RUTAS DISPONIBLES - Sistema Garritas Veterinaria

## 🌐 RUTAS WEB (Páginas HTML)

### Rutas Principales
- **`/`** → Redirige a `/web/login`
- **`/login`** → Página de login
- **`/web/login`** → Página de login (ruta alternativa)

### Dashboard
- **`/dashboard`** → Dashboard principal
- **`/web/dashboard`** → Dashboard web (ruta alternativa)

### Gestión de Entidades
- **`/web/usuarios`** → Gestión de usuarios
- **`/web/mascotas`** → Gestión de mascotas  
- **`/web/citas`** → Gestión de citas
- **`/web/inventario`** → Gestión de inventario
- **`/web/reportes`** → Reportes
- **`/web/historial`** → Historial clínico

### Rutas de Compatibilidad
- **`/usuarios-web`** → Alias para `/web/usuarios`
- **`/mascotas-web`** → Alias para `/web/mascotas`
- **`/citas-web`** → Alias para `/web/citas`
- **`/inventario-web`** → Alias para `/web/inventario`

---

## 🔌 RUTAS API (JSON)

### Autenticación
- **`POST /auth/login`** → Login de usuarios
- **`POST /auth/register`** → Registro de usuarios (si está implementado)

### Usuarios API
- **`GET /usuarios/count`** → Cantidad de usuarios
- **`GET /usuarios/`** → Listar todos los usuarios
- **`GET /usuarios/{id}`** → Obtener usuario por ID
- **`POST /usuarios/`** → Crear nuevo usuario
- **`PUT /usuarios/{id}`** → Actualizar usuario
- **`DELETE /usuarios/{id}`** → Eliminar usuario

### Mascotas API
- **`GET /mascotas/`** → Listar todas las mascotas
- **`GET /mascotas/{id}`** → Obtener mascota por ID
- **`POST /mascotas/`** → Crear nueva mascota
- **`PUT /mascotas/{id}`** → Actualizar mascota
- **`DELETE /mascotas/{id}`** → Eliminar mascota

### Citas API
- **`GET /citas/`** → Listar todas las citas
- **`GET /citas/{id}`** → Obtener cita por ID
- **`POST /citas/`** → Crear nueva cita
- **`PUT /citas/{id}`** → Actualizar cita
- **`DELETE /citas/{id}`** → Eliminar cita

### Inventario/Productos API
- **`GET /productos/`** → Listar todos los productos
- **`GET /productos/{id}`** → Obtener producto por ID
- **`POST /productos/`** → Crear nuevo producto
- **`PUT /productos/{id}`** → Actualizar producto
- **`DELETE /productos/{id}`** → Eliminar producto

### Servicios API
- **`GET /servicios/{id}`** → Obtener servicio por ID

---

## 🛠️ RUTAS DE SISTEMA

### Health Check
- **`GET /health`** → Estado del servidor (si está implementado)

### Error Handling
- **`/error`** → Página de error personalizada

### Recursos Estáticos
- **`/static/**`** → Archivos estáticos (CSS, JS, imágenes)
- **`/css/**`** → Archivos CSS
- **`/js/**`** → Archivos JavaScript
- **`/images/**`** → Imágenes

---

## ⚙️ CONFIGURACIÓN ACTUAL

### Estado del Servidor
- **Puerto**: 8080
- **PID**: 13428
- **Estado**: ✅ EJECUTÁNDOSE

### Seguridad
- **JWT**: ❌ DESHABILITADO (`jwt.enabled=false`)
- **Autenticación**: ⚠️ TODAS LAS RUTAS PÚBLICAS (modo desarrollo)

### Base de Datos
- **Tipo**: MySQL/MariaDB
- **Host**: localhost:3306
- **Database**: garritas_veterinaria

---

## 🔍 PRUEBAS RÁPIDAS

Para probar las rutas, puedes acceder directamente desde el navegador:

1. **Página principal**: http://localhost:8080/
2. **Dashboard**: http://localhost:8080/dashboard  
3. **Usuarios**: http://localhost:8080/web/usuarios
4. **Mascotas**: http://localhost:8080/web/mascotas
5. **Inventario**: http://localhost:8080/web/inventario

---

## ⚠️ NOTAS IMPORTANTES

1. **Historial Clínico**: Pendiente de implementar (línea 210 en WebViewController)
2. **JWT**: Deshabilitado temporalmente para desarrollo
3. **BCrypt**: Usando texto plano temporalmente en UsuarioService
4. **Seguridad**: Todas las rutas son públicas actualmente

---

## 🐛 PROBLEMAS RESUELTOS

- ✅ Error `ERR_INCOMPLETE_CHUNKED_ENCODING` 
- ✅ Página en blanco
- ✅ Puerto 8080 ocupado
- ✅ Configuración de seguridad conflictiva