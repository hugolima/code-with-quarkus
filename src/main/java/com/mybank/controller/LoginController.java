package com.mybank.controller;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mybank.dto.LoginDTO;
import com.mybank.service.ClienteService;

@Path("/login")
public class LoginController {

    @Inject
    protected ClienteService clienteService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginData) throws URISyntaxException {
        String jwt = clienteService.createJWTTokenForLogin(loginData.cpf, loginData.senha);
        return Response.ok()
                .header("Set-Cookie", "jwt=" + jwt + "; SameSite=strict; HttpOnly")
                .build();
    }
}
