package itch.dlc.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import itch.dlc.model.Perfil;
import itch.dlc.repository.PerfilRepository;
import itch.dlc.service.PerfilService;

@Primary
@Service
public class PerfilServiceJpa implements PerfilService {

    @Autowired
    private PerfilRepository repo;

    @Override
    public List<Perfil> buscarTodosPerfil() {
        return repo.findAll();
    }

    @Override
    public Perfil buscarPorIdPerfil(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void guardarPerfil(Perfil perfil) {
        repo.save(perfil);
    }

    @Override
    public void eliminarPerfil(Integer id) {
        repo.deleteById(id);
    }
}
