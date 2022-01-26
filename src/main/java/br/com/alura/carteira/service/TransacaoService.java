package br.com.alura.carteira.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository transacaoRespository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CalculadoraDeImpostoService calculadoraDeImposto;
	
	@Autowired
	private ModelMapper modelMapper;

	public Page<TransacaoDto> listar(Pageable paginacao, Usuario usuario) {
		return transacaoRespository.findAllByUsuario(paginacao, usuario)
				.map(t -> modelMapper.map(t, TransacaoDto.class));
//		
//		List<TransacaoDto> transacoesDto = new ArrayList<>();
//		transacoes.forEach(transacao -> {
//			BigDecimal imposto = calculadoraDeImposto.calcular(transacao);
//			TransacaoDto dto = modelMapper.map(transacao, TransacaoDto.class);
//			dto.setImposto(imposto);
//			transacoesDto.add(dto);
//		});
//		return new PageImpl<TransacaoDto>(transacoesDto, 
//				transacoes.getPageable(),
//				transacoes.getTotalElements());
	}

	@Transactional
	public TransacaoDto cadastrar(@Valid TransacaoFormDto dto, Usuario logado) {
		Long idUsuario = dto.getUsuarioId();

		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			if(!usuario.equals(logado)) {
				lancarErrorAcessoNegado();
			}
			
			Transacao transacao = modelMapper.map(dto, Transacao.class);
			transacao.setId(null);
			transacao.setUsuario(usuario);
			BigDecimal imposto = calculadoraDeImposto.calcular(transacao);
			transacao.setImposto(imposto);

			transacaoRespository.save(transacao);
			return modelMapper.map(transacao, TransacaoDto.class);
		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException("Usuario inexistente!");
		}
	}
	
	@Transactional
	public TransacaoDto atualizar(@Valid AtualizacaoTransacaoFormDto dto, Usuario logado) {
		Transacao transacao = transacaoRespository.getById(dto.getId());
		if(!transacao.pertenceAoUsuario(logado)) {
			lancarErrorAcessoNegado();
		}
		
		transacao.atualizarInformacoes(dto.getTicker(), dto.getData(), dto.getPreco(),dto.getQuantidade(),dto.getTipo());
		return modelMapper.map(transacao, TransacaoDto.class);
	}
	
	@Transactional
	public void remover(@NotNull Long id, Usuario logado) {
		Transacao transacao = transacaoRespository.getById(id);
		if(!transacao.pertenceAoUsuario(logado)) {
			lancarErrorAcessoNegado();
		}
		transacaoRespository.deleteById(id);
	}

	public TransacaoDetalhadaDto detalhar(Long id, Usuario logado) {
		Transacao transacao = transacaoRespository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		
		if(!transacao.pertenceAoUsuario(logado)) {
			lancarErrorAcessoNegado();
		}
		
		return modelMapper.map(transacao, TransacaoDetalhadaDto.class);
	}
	
	private void lancarErrorAcessoNegado() {
		throw new AccessDeniedException("Acesso negado!");
	}
}
