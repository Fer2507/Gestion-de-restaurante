package itch.dlc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.dlc.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
	Optional<Perfil> findByPerfil(String perfil);
}
