package com.mycompany.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataSourceConfigTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() throws SQLException {
        assertNotNull(dataSource);
        
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testDataLoaded() {
        // Verificar que los datos del archivo data-test.sql se cargaron
        Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuario", Integer.class);
        assertNotNull(userCount);
        assertEquals(3, userCount);
        
        Integer roleCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rol", Integer.class);
        assertNotNull(roleCount);
        assertEquals(3, roleCount);
        
        Integer serviceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM servicio", Integer.class);
        assertNotNull(serviceCount);
        assertEquals(3, serviceCount);
        
        Integer petCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mascota", Integer.class);
        assertNotNull(petCount);
        assertEquals(3, petCount);
    }
    
    @Test
    public void testUserRoleRelationship() {
        // Verificar la relación entre usuario y rol
        List<Map<String, Object>> admins = jdbcTemplate.queryForList(
                "SELECT u.* FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE r.nombre = 'ADMIN'");
        
        assertEquals(1, admins.size());
        assertEquals("admin", admins.get(0).get("USUARIO"));
    }
}