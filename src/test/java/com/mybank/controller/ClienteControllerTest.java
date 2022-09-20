package com.mybank.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.mybank.dto.CreateClienteDTO;
import com.mybank.model.Cliente;
import com.mybank.util.ClienteDatabaseUtil;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class ClienteControllerTest {
    
    private final String CPF = "11111111111";
    private final String NOME = "Nome";
    private final String SENHA = "123456";
    
    @Inject
    protected ClienteDatabaseUtil clienteDatabaseUtil;
    
    @Test
    public void testCreateClienteCpfNulo() {
        CreateClienteDTO clienteData = new CreateClienteDTO();
        
        given()
            .body(clienteData)
            .header("Content-Type", "application/json")
            .when().post("/cliente")
            .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", is(1))
                .body("message", is("CPF é obrigatório"));
    }
    
    @Test
    public void testCreateClienteNomeNulo() {
        CreateClienteDTO clienteData = new CreateClienteDTO();
        clienteData.cpf = CPF;
        
        given()
            .body(clienteData)
            .header("Content-Type", "application/json")
            .when().post("/cliente")
            .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", is(1))
                .body("message", is("Nome é obrigatório"));
    }
    
    @Test
    public void testCreateClienteSenhaNulo() {
        CreateClienteDTO clienteData = new CreateClienteDTO();
        clienteData.cpf = CPF;
        clienteData.nome = NOME;
        
        given()
            .body(clienteData)
            .header("Content-Type", "application/json")
            .when().post("/cliente")
            .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", is(1))
                .body("message", is("Senha é obrigatório"));
    }
    
    @Test
    public void testCreateClienteJaExistente() {
        CreateClienteDTO clienteData = new CreateClienteDTO();
        clienteData.cpf = CPF;
        clienteData.nome = NOME;
        clienteData.senha = SENHA;
        
        clienteDatabaseUtil.createCliente(clienteData);
        
        given()
            .body(clienteData)
            .header("Content-Type", "application/json")
            .when().post("/cliente")
            .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", is(2))
                .body("message", is("Cliente já cadastrado"));
        
        clienteDatabaseUtil.deleteCliente(clienteData.cpf);
    }
    
    @Test
    @TestTransaction
    public void testCreateClienteSuccess() {
        CreateClienteDTO clienteData = new CreateClienteDTO();
        clienteData.cpf = CPF;
        clienteData.nome = NOME;
        clienteData.senha = SENHA;
        
        given()
            .body(clienteData)
            .header("Content-Type", "application/json")
            .when().post("/cliente")
            .then()
                .statusCode(204);
        
        Cliente clientePersistido = clienteDatabaseUtil.getCliente(clienteData.cpf);
        assertEquals(clientePersistido.cpf, CPF);
        assertEquals(clientePersistido.nome, NOME);
        assertEquals(clientePersistido.senha, SENHA);
    }
}
