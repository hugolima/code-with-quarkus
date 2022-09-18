package com.mybank.service;

import java.time.Duration;
import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Cookie;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.mybank.dto.ClienteDTO;
import com.mybank.model.Cliente;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class ClienteService {
	
	@Inject
	private JWTParser jwtParser;
	
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
			throw new RuntimeException("CPF é obrigatório");
		}
		if (clienteData.nome == null || clienteData.nome.isBlank()) {
			throw new RuntimeException("Nome é obrigatório");
		}
		if (clienteData.senha == null || clienteData.senha.isBlank()) {
			throw new RuntimeException("Senha é obrigatório");
		}
	}
	
	public String createJWTTokenForLogin(String cpf, String senha) {
		Cliente cliente = (Cliente) Cliente.find("cpf = ?1 and senha = ?2", cpf, senha)
				.singleResultOptional().orElse(null);
		if (cliente != null) {
			return createJWTToken(cliente.cpf);			
		}
		return null;
	}
	
	private String createJWTToken(String subject) {
		return Jwt.subject(subject)
				.expiresIn(Duration.ofDays(360))
				.issuedAt(Instant.now())
				.signWithSecret(jwtSecret);
	}

	public Cliente getClienteLoggedIn(Cookie jwt) {
		if (jwt == null) {
			throw new RuntimeException("Usuário não logado");
		}
		
		try {
			
			JsonWebToken jwtToken = jwtParser.verify(jwt.getValue(), jwtSecret);
			PanacheEntityBase cliente = Cliente.findById(jwtToken.getSubject());
			if (cliente != null) {
				return (Cliente) cliente;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Usuário não logado");
	}
}
