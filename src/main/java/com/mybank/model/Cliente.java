package com.mybank.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Cliente extends PanacheEntityBase {
	
	@Id
	public String cpf;
	public String nome;
	public String senha;
}
