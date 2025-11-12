# 📋 Actualización del Modelo HistorialClinico

## ✅ Cambios Realizados

### 1️⃣ Script SQL Creado: `fix_historial_clinico.sql`

**Nuevas columnas agregadas:**
- `fecha_consulta` (datetime) - Fecha y hora de la consulta
- `tipo_consulta` (ENUM) - Valores: CONSULTA, VACUNACION, CIRUGIA, EMERGENCIA, CONTROL
- `motivo_consulta` (varchar 500) - Razón de la visita
- `medicamentos` (varchar 500) - Medicamentos recetados
- `estado` (ENUM) - Valores: COMPLETADO, PENDIENTE, EN_PROGRESO
- `costo` (decimal 10,2) - Costo de la consulta
- `id_veterinario` (int unsigned) - FK a usuario (veterinario)

**Foreign Key agregada:**
- `fk_historial_veterinario` → Relaciona con `usuario.id_usuario`

### 2️⃣ Modelo Java Actualizado: `HistorialClinico.java`

**Nuevos campos agregados:**
```java
private Usuario veterinario;              // ManyToOne
private LocalDateTime fechaConsulta;      
private TipoConsulta tipoConsulta;        // ENUM
private String motivoConsulta;            // max 500 chars
private String medicamentos;              // max 500 chars
private EstadoConsulta estado;            // ENUM
private BigDecimal costo;
```

**Enums definidos:**
```java
public enum TipoConsulta {
    CONSULTA, VACUNACION, CIRUGIA, EMERGENCIA, CONTROL
}

public enum EstadoConsulta {
    COMPLETADO, PENDIENTE, EN_PROGRESO
}
```

## 🚀 Pasos para Aplicar

### Paso 1: Ejecutar el script SQL
**Opción A - MySQL Workbench:**
1. Abre MySQL Workbench
2. Conecta a localhost
3. Abre `fix_historial_clinico.sql`
4. Ejecuta el script completo (⚡ o Ctrl+Shift+Enter)

**Opción B - phpMyAdmin:**
1. Abre phpMyAdmin
2. Selecciona base de datos `garritas_veterinaria`
3. Pestaña SQL
4. Pega el contenido de `fix_historial_clinico.sql`
5. Ejecutar

**Opción C - Línea de comandos:**
```powershell
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -uroot garritas_veterinaria < fix_historial_clinico.sql
```

### Paso 2: Compilar la aplicación
```powershell
.\mvnw.cmd clean package -DskipTests
```

### Paso 3: Reiniciar el servidor
- Detén el servidor actual (Ctrl+C en la terminal "Run: VeterinariaApplication")
- Vuelve a ejecutar la aplicación

### Paso 4: Verificar
1. Accede a http://localhost:8080/historial
2. La página debería cargar sin errores
3. Los datos existentes deberían mostrarse correctamente

## 📊 Mapeo Completo

| Campo MySQL | Tipo MySQL | Campo Java | Tipo Java |
|-------------|------------|------------|-----------|
| id_historial | bigint(20) | id | Long |
| fecha | datetime(6) | fecha | LocalDateTime |
| fecha_consulta | datetime(6) | fechaConsulta | LocalDateTime |
| tipo_consulta | enum | tipoConsulta | TipoConsulta |
| motivo_consulta | varchar(500) | motivoConsulta | String |
| diagnostico | varchar(255) | diagnostico | String |
| tratamiento | varchar(255) | tratamiento | String |
| medicamentos | varchar(500) | medicamentos | String |
| observaciones | varchar(255) | observaciones | String |
| estado | enum | estado | EstadoConsulta |
| costo | decimal(10,2) | costo | BigDecimal |
| notas | varchar(255) | notas | String |
| id_mascota | int unsigned | mascota | Mascota (ManyToOne) |
| id_veterinario | int unsigned | veterinario | Usuario (ManyToOne) |

## ⚠️ Notas Importantes

1. **Migración de datos**: El script copia automáticamente `fecha` a `fecha_consulta` para los registros existentes
2. **Valores por defecto**: Los registros existentes se marcarán como `tipo_consulta='CONSULTA'` y `estado='COMPLETADO'`
3. **id_veterinario**: Quedará NULL para los registros existentes. Deberás actualizarlo manualmente si es necesario
4. **Compatibilidad**: La columna `fecha` original se mantiene para compatibilidad hacia atrás

## ✅ Verificación de Éxito

Después de aplicar los cambios, verifica que:
- [ ] La tabla MySQL tiene todas las nuevas columnas
- [ ] La aplicación compila sin errores
- [ ] La página `/historial` carga correctamente
- [ ] Los 5 registros existentes se muestran en la tabla
- [ ] Puedes crear nuevos registros con todos los campos

## 🐛 Solución de Problemas

**Error: "Column already exists"**
- El script usa `ADD COLUMN IF NOT EXISTS`, así que es seguro ejecutarlo múltiples veces

**Error: "Cannot add foreign key constraint"**
- Verifica que la tabla `usuario` exista y tenga usuarios con rol VETERINARIO

**Error en compilación Java**
- Ejecuta: `.\mvnw.cmd clean compile`
- Verifica que importaste `java.math.BigDecimal`

**Página no carga**
- Revisa `logs/application.log` para ver errores específicos
- Verifica que el servidor se reinició después de los cambios
