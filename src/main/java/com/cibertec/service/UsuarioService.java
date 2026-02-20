package com.cibertec.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cibertec.dto.RangoEdadDTO;
import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public Usuario login(String username, String password){
		
		Usuario usuario = usuarioRepository
		        .findByUsrUsuaAndClaUsua(username, password)
		        .orElseThrow(() -> new RuntimeException("Credenciales son incorrectas. Intente de nuevo."));
		
		// Validar que tenga tipo asignado
	    if(usuario.getTipo() == null) {
	        throw new RuntimeException("El usuario no tiene un tipo asignado.");
	    }
		return usuario;
	}
	
	public void guardar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	
	public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
	
	public Usuario buscarPorId(Integer id) {
	    return usuarioRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}
	
	public List<RangoEdadDTO> obtenerRangoEdades() {
	    List<Usuario> usuarios = listar();
	    int anioActual = LocalDate.now().getYear();

	    long menorDe20 = 0, de20a29 = 0, de30a39 = 0, mayorDe40 = 0;

	    for (Usuario u : usuarios) {
	        if (u.getFnaUsua() != null) {
	            int edad = anioActual - u.getFnaUsua().getYear();
	            if      (edad < 20) menorDe20++;
	            else if (edad < 30) de20a29++;
	            else if (edad < 40) de30a39++;
	            else                mayorDe40++;
	        }
	    }

	    return List.of(
	        new RangoEdadDTO("Menor de 20", menorDe20),
	        new RangoEdadDTO("20 - 29",     de20a29),
	        new RangoEdadDTO("30 - 39",     de30a39),
	        new RangoEdadDTO("40 o más",    mayorDe40)
	    );
	}

}
