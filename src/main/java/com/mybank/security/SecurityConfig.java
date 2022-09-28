package com.mybank.security;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "security")
public interface SecurityConfig {

    Jwt jwt();
    
    interface Jwt {
        String key();
    }
}
