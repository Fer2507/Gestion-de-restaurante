package itch.dlc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import itch.dlc.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	 // Cliente por nombre exacto
    Optional<Cliente> findByNombre(String nombre);

    // Clientes cuyo nombre contenga una cadena específica
    List<Cliente> findByNombreContaining(String cadena);

    // Cliente por email exacto
    Optional<Cliente> findByEmail(String email);

    // Clientes cuyo email termine con "@gmail.com"
    List<Cliente> findByEmailEndingWith(String dominio);

    // Clientes con crédito entre dos valores
    List<Cliente> findByCreditoBetween(Double min, Double max);

    // Clientes con crédito mayor a un valor
    List<Cliente> findByCreditoGreaterThan(Double monto);

    // Clientes destacados (destacado = 1)
    List<Cliente> findByDestacado(int destacado);

    // Clientes cuyo nombre contenga una palabra y tengan crédito mayor a un valor
    List<Cliente> findByNombreContainingAndCreditoGreaterThan(String nombre, Double credito);

    // Clientes cuya foto sea "no_imagen.jpg"
    @Query("SELECT c FROM Cliente c WHERE c.nombreFoto = 'no_imagen.jpg' OR c.nombreFoto IS NULL")
    List<Cliente> findByNombreFoto(String nombreFoto);

    // Clientes destacados con crédito mayor a un valor
    List<Cliente> findByDestacadoAndCreditoGreaterThan(int destacado, Double credito);

    // 5 clientes con mayor crédito
    @Query("SELECT c FROM Cliente c ORDER BY c.credito DESC LIMIT 5")
    List<Cliente> findTop5ByOrderByCreditoDesc();
    
    Optional<Cliente> findByUsuario_IdUsuario(Integer idUsuario);

}
