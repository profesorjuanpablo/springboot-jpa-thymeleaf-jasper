package com.cibertec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cibertec.model.Tipo;
import com.cibertec.repository.TipoRepository;

@Service
public class TipoService {
	private final TipoRepository tipoRepository;

	public TipoService(TipoRepository tipoRepository) {
		this.tipoRepository = tipoRepository;
	}
	
	public List<Tipo> listar(){
		return tipoRepository.findAll();
	}	
}
