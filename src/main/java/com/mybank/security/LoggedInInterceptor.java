package com.mybank.security;

import java.io.IOException;
import java.security.Principal;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.mybank.exception.SecurityException;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;

@LoggedIn
@Provider
public class LoggedInInterceptor implements ContainerRequestFilter {
    
    @Inject
    protected JWTParser jwtParser;
    
    @Inject
    protected Instance<SecurityConfig> securityConfig;
    
    @Context
    protected ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        Cookie jwt = ctx.getCookies().get("jwt");
        if (jwt == null) {
            throw new SecurityException(2, "Token de sessão não encontrado.");
        }
        
        LoggedIn annotation = resourceInfo.getResourceMethod().getAnnotation(LoggedIn.class);
        String tipo = annotation.value();
        JsonWebToken jwtToken;
        
        try {
            jwtToken = jwtParser.verify(jwt.getValue(), securityConfig.get().jwt().key());
            Principal subject = new MyBankPrincipal(jwtToken.getSubject());
            ctx.setSecurityContext(new SecurityContext() {
                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }
                @Override
                public boolean isSecure() {
                    return true;
                }
                @Override
                public Principal getUserPrincipal() {
                    return subject;
                }
                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });
        } catch (ParseException e) {
            throw new SecurityException(2, "Token de sessão inválido.");
        }
        
        System.out.println("Role: " + tipo);
        System.out.println(jwtToken.getSubject());
        System.out.println(jwtToken.getClaim("type").toString());
    }
    
    private static class MyBankPrincipal implements Principal {

        private final String subject;
        
        public MyBankPrincipal(String subject) {
            this.subject = subject;
        }

        @Override
        public String getName() {
            return subject;
        }
    }
}
