package br.com.alura.carteira.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioFormDto {
		
	@NotNull
	private String nome;
	@NotNull
	private String login;
	
	private String senha;
	
	@NotNull
	private Long perfilId;
	
}
