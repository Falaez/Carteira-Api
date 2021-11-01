package br.com.alura.carteira.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	
	private ModelMapper modelMapper = new ModelMapper();
	
	public Page<TransacaoDto> listar(Pageable paginacao){
		Page<Transacao> transacao = transacaoRespository.findAll(paginacao);
		return transacao.map(t -> modelMapper.map(t, TransacaoDto.class));
	}
	
	@Transactional
	public TransacaoDto cadastrar(@Valid TransacaoFormDto dto) {
	Long idUsuario = dto.getUsuarioId();
	
	try {
	Usuario usuario = usuarioRepository.getById(idUsuario);
	
	Transacao transacao= modelMapper.map(dto, Transacao.class);
	transacao.setId(null);
	transacao.setUsuario(usuario);
	
	transacaoRespository.save(transacao);
	return modelMapper.map(transacao, TransacaoDto.class);
	}
	catch (EntityNotFoundException e) {
		throw new IllegalArgumentException("Usuario inexistente!");
	}
	}
}
