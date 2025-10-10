package com.mycompany.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para las propiedades de JWT.
 * Esto elimina las advertencias de "unknown property" en los archivos .properties
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    private boolean enabled = true;
    private String secret = "mySecretKey";
    private long expiration = 86400000; // 24 horas en milisegundos

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
