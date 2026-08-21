package itch.dlc.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import itch.dlc.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByClienteId(Integer idCliente);
	
	//Pedidos asociados a una reserva
    List<Pedido> findByReserva_IdReserva(Integer idReserva);

    // Pedidos sin reserva
    List<Pedido> findByReservaIsNull();
    
    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT p FROM Pedido p JOIN p.atenciones a WHERE a.empleado.idEmpleado = :idEmpleado")
    List<Pedido> findByEmpleado(@Param("idEmpleado") Integer idEmpleado);
    
    @Query(" SELECT p FROM Pedido p LEFT JOIN p.reserva r WHERE (p.cliente.id = :idCliente) OR (p.reserva IS NOT NULL AND p.reserva.cliente.id = :idCliente)")
    List<Pedido> findByClientes(@Param("idCliente") Integer idCliente);
    
    
}
