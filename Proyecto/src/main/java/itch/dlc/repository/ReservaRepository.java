package itch.dlc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import itch.dlc.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	List<Reserva> findByEstatus(String estatus);
	
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " + "FROM Reserva r " +
	"WHERE r.mesa.idMesa = :idMesa AND r.fechaHora = :fecha AND r.idReserva <> :idReserva")
		boolean existsByMesaIdAndFechaExcludingReserva(@Param("idMesa") Integer idMesa,
		                                               @Param("fecha") LocalDateTime fecha,
		                                               @Param("idReserva") Integer idReserva);


	// Buscar por fecha exacta
	@Query("SELECT r FROM Reserva r WHERE r.fechaHora BETWEEN :inicio AND :fin")
	List<Reserva> findByFechaExacta(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Buscar por rango de fechas
    @Query("SELECT r FROM Reserva r WHERE r.fechaHora BETWEEN :inicio AND :fin")
    List<Reserva> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Buscar por capacidad de mesa
    @Query("SELECT r FROM Reserva r WHERE r.mesa.capacidad = :capacidad")
    List<Reserva> findByMesaCapacidad(@Param("capacidad") int capacidad);
    
    List<Reserva> findByClienteIdAndEstatus(Integer clienteId, String estatus);
}
