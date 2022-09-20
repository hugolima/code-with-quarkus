package com.mybank.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.mybank.dto.CreateClienteDTO;
import com.mybank.model.Cliente;
import com.mybank.service.ClienteService;

@ApplicationScoped
public class ClienteDatabaseUtil {

    @Inject
    protected ClienteService clienteService;
    
    @Transactional
    public void createCliente(CreateClienteDTO clienteData) {
        clienteService.createCliente(clienteData);
    }
    
    @Transactional
    public void deleteCliente(String cpf) {
        Cliente.delete("cpf = ?1", cpf);
    }
    
    public Cliente getCliente(String cpf) {
        return Cliente.findById(cpf);
    }
}
