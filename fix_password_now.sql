USE garritas_veterinaria;

-- Actualizar la contraseña del admin
-- Hash BCrypt de '123456': $2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW
UPDATE usuario 
SET contrasena = '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW' 
WHERE usuario = 'admin';

-- Verificar el cambio
SELECT id_usuario, usuario, contrasena, id_rol 
FROM usuario 
WHERE usuario = 'admin';
