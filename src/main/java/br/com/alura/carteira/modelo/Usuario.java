package br.com.alura.carteira.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"senha"})
public class Usuario {
		
		
	private String nome;
	private String login;
	private String senha;
}
