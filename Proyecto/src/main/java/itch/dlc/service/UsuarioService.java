package itch.dlc.service;

import java.util.List;

import itch.dlc.model.Usuario;

public interface UsuarioService {
	List<Usuario> listarUsuarios();
    Usuario buscarPorIdUsuario(Integer idUsuario);
    Usuario guardarUsuarios(Usuario usuario);
    void eliminarUsuario(Integer idUsuario);
}
