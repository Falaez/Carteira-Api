package br.com.alura.carteira.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.carteira.infra.security.TokenService;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.PerfilRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest //carrega o projeto todo
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional//fazer de um modo transacional e fazer o rollback de cada teste
class UsuarioControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String token;
	
	@BeforeEach
	public void gerarToken() {
		Usuario logado = new Usuario("Mario","mario@email.com","123456");
		Perfil admin = perfilRepository.findById(1l).get();
		logado.adicionarPerfil(admin);
		usuarioRepository.save(logado);
		Authentication authentication = new UsernamePasswordAuthenticationToken(logado, logado.getLogin());
		this.token = tokenService.gerarToken(authentication);
	}
	
	@Test
	void naoDeveriaCadastrarUsuarioComDadosIncompletos() throws Exception {
		String json = "{}";
		
		
		mvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON).content(json)
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isBadRequest()); 
	}
	
	
	@Test
    void deveriaCadastrarUsuarioComDadosCompletos() throws Exception {
        String json = "{\"nome\":\"fulano\",\"login\":\"fulano@gmail.com\",\"perfilId\":1, \"email\":\"fulano@email.com\"}";
        String jsonEsperado = "{\"nome\":\"fulano\",\"login\":\"fulano@gmail.com\"}";

        mvc
                .perform(
                        post("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .header("Authorization", "Bearer " + token))

                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().json(jsonEsperado));

    }

}
