package itch.dlc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import itch.dlc.model.Usuario;
import jakarta.transaction.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	 @Modifying
	    @Transactional
	    @Query(
	        value = "INSERT INTO UsuarioPerfil (idUsuario, idPerfil) VALUES (:idUsuario, :idPerfil)",
	        nativeQuery = true
	    )
	    void asignarPerfil(@Param("idUsuario") Integer idUsuario, @Param("idPerfil") Integer idPerfil);

	 Optional<Usuario> findByEmail(String email);

	Usuario findByUsername(String username);
}
