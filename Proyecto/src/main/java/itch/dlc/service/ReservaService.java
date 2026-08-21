package itch.dlc.service;

import java.util.List;

import itch.dlc.model.Reserva;

import java.time.LocalDateTime;

public interface ReservaService {
	 // Crear o actualizar una reserva
    Reserva actualizar(Reserva reserva);

    // Guardar una nueva reserva
    Reserva guardar(Reserva reserva);

    // Eliminar una reserva por su ID
    void eliminar(Integer id);

    // Buscar reserva por ID
    Reserva buscarPorId(Integer id);

    // Listar todas las reservas
    List<Reserva> listarTodas();

    // Listar reservas por estatus (pendiente, confirmada, cancelada)
    List<Reserva> listarPorEstatus(String estatus);

    // Cambiar el estatus de una reserva
    void cambiarEstatus(Integer id, String nuevoEstatus);
    
    boolean mesaOcupada(Integer idMesa, LocalDateTime fechaHora, Integer idReserva);

    // Poner reserva en pendiente
    default void ponerPendiente(Integer id) {
        cambiarEstatus(id, "Pendiente");
    }
    
    List<Reserva> obtenerReservasPorCliente(Integer idCliente, String estatus);
}
