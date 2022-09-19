package com.mybank.service;

import static java.time.Duration.ofDays;
import static java.time.Instant.now;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Cookie;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.mybank.dto.ClienteDTO;
import com.mybank.exception.BusinessException;
import com.mybank.exception.SecurityException;
import com.mybank.model.Cliente;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class ClienteService {

    @Inject
    protected JWTParser jwtParser;

    private final String jwtSecret = "KiTHOH4xtiVowIBGN+XXZi0ZYwTVWXZkUDhCRtQMstU=";

    public void createCliente(ClienteDTO clienteData) {
        validateCreateCliente(clienteData);

        Cliente cliente = new Cliente();
        cliente.cpf = clienteData.cpf;
        cliente.nome = clienteData.nome;
        cliente.senha = clienteData.senha;

        cliente.persistAndFlush();
    }

    private void validateCreateCliente(ClienteDTO clienteData) {
        if (clienteData.cpf == null || clienteData.cpf.isBlank()) {
            throw new BusinessException(1, "CPF é obrigatório");
        }
        if (clienteData.nome == null || clienteData.nome.isBlank()) {
            throw new BusinessException(1, "Nome é obrigatório");
        }
        if (clienteData.senha == null || clienteData.senha.isBlank()) {
            throw new BusinessException(1, "Senha é obrigatório");
        }
        if (Cliente.count("cpf = ?1", clienteData.cpf) > 0) {
            throw new BusinessException(2, "Cliente já cadastrado");
        }
    }

    public String createJWTTokenForLogin(String cpf, String senha) {
        Cliente cliente = (Cliente) Cliente.find("cpf = ?1 and senha = ?2", cpf, senha).singleResultOptional().orElse(null);
        if (cliente != null) {
            return createJWTToken(cliente.cpf);
        }
        throw new SecurityException(1, "Usuário e/ou senha inválidos.");
    }

    private String createJWTToken(String subject) {
        return Jwt.subject(subject).expiresIn(ofDays(360)).issuedAt(now()).signWithSecret(jwtSecret);
    }

    public Cliente getClienteLoggedIn(Cookie jwt) {
        if (jwt == null) {
            throw new SecurityException(2, "Token de sessão não encontrado.");
        }

        try {

            JsonWebToken jwtToken = jwtParser.verify(jwt.getValue(), jwtSecret);
            PanacheEntityBase cliente = Cliente.findById(jwtToken.getSubject());
            if (cliente != null) {
                return (Cliente) cliente;
            }

        } catch (ParseException e) {
            throw new SecurityException(3, "Problemas no Token de sessão.");
        }

        throw new SecurityException(4, "Usuário não logado.");
    }
}
