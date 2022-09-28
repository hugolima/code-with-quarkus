package com.mybank.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.mybank.dto.ClienteInfoDTO;
import com.mybank.dto.CreateClienteDTO;
import com.mybank.model.Cliente;
import com.mybank.security.LoggedIn;
import com.mybank.service.ClienteService;

@Path("/cliente")
public class ClienteController {

    @Inject
    protected ClienteService clienteService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void createClient(CreateClienteDTO clienteData) {
        clienteService.createCliente(clienteData);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @LoggedIn("CLT")
    public ClienteInfoDTO getClienteInfo(@Context SecurityContext security) {
        Cliente cliente = clienteService.getCliente(security.getUserPrincipal().getName());
        ClienteInfoDTO result = new ClienteInfoDTO();
        result.cpf = cliente.cpf;
        result.nome = cliente.nome;
        return result;
    }
}
