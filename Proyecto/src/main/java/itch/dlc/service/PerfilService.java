package itch.dlc.service;

import java.util.List;

import itch.dlc.model.Perfil;

public interface PerfilService {
	List<Perfil> buscarTodosPerfil();

    Perfil buscarPorIdPerfil(Integer id);

    void guardarPerfil(Perfil perfil);

    void eliminarPerfil(Integer id);

}
