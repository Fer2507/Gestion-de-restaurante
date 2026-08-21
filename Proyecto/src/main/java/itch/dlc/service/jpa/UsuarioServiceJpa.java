package itch.dlc.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import itch.dlc.model.Usuario;
import itch.dlc.repository.UsuarioRepository;
import itch.dlc.service.UsuarioService;

@Primary
@Service
public class UsuarioServiceJpa implements UsuarioService{

	    @Autowired
	    private UsuarioRepository usuarioRepo;
	    
	    @Override
	    public List<Usuario> listarUsuarios() {
	        return usuarioRepo.findAll();
	    }

	    @Override
	    public Usuario buscarPorIdUsuario(Integer idUsuario) {
	        return usuarioRepo.findById(idUsuario).orElse(null);
	    }

	    @Override
	    public Usuario guardarUsuarios(Usuario usuario) {
	       return usuarioRepo.save(usuario);
	    }

	    @Override
	    public void eliminarUsuario(Integer idUsuario) {
	        usuarioRepo.deleteById(idUsuario);
	    }
}
