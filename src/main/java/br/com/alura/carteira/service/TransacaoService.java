package br.com.alura.carteira.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.repository.TransacaoRepository;

@Service
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRespository;
	private ModelMapper modelMapper = new ModelMapper();
	
	public List<TransacaoDto> listar(){
		List<Transacao> transacao = transacaoRespository.findAll();
		return transacao.stream().map(t -> modelMapper.map(t, TransacaoDto.class)).collect(Collectors.toList());
	}
	
	@Transactional
	public void cadastrar(@Valid TransacaoFormDto dto) {
	Transacao transacao= modelMapper.map(dto, Transacao.class);
	transacao.setId(null);
	
	transacaoRespository.save(transacao);
	}
}
