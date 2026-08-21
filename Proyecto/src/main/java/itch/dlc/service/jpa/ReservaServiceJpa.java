package itch.dlc.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


import itch.dlc.model.Reserva;
import itch.dlc.repository.ReservaRepository;
import itch.dlc.service.ReservaService;

import java.util.List;

@Primary
@Service
public class ReservaServiceJpa implements ReservaService {

	  @Autowired
	    private ReservaRepository reservarRepository;

	    @Override
	    public Reserva guardar(Reserva reserva) {
	        return reservarRepository.save(reserva);
	    }

	    @Override
	    public Reserva actualizar(Reserva reserva) {
	        return reservarRepository.save(reserva);
	    }

	    @Override
	    public void eliminar(Integer id) {
	        reservarRepository.deleteById(id);
	    }

	    @Override
	    public Reserva buscarPorId(Integer id) {
	        return reservarRepository.findById(id).orElse(null);
	    }

	    @Override
	    public List<Reserva> listarTodas() {
	        return reservarRepository.findAll();
	    }

	    @Override
	    public List<Reserva> listarPorEstatus(String estatus) {
	        return reservarRepository.findByEstatus(estatus);
	    }

	    @Override
	    public void cambiarEstatus(Integer id, String nuevoEstatus) {
	        Reserva reserva = reservarRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
	        reserva.setEstatus(nuevoEstatus);
	        reservarRepository.save(reserva);
	    }
	    
	    @Override
	    public boolean mesaOcupada(Integer idMesa, LocalDateTime fechaHora, Integer idExcluido) {
	        return reservarRepository.existsByMesaIdAndFechaExcludingReserva(idMesa, fechaHora, idExcluido);
	    }
	    
	    @Override
	    public List<Reserva> obtenerReservasPorCliente(Integer idCliente, String estatus) {
	        return reservarRepository.findByClienteIdAndEstatus(idCliente, estatus );
	    }
}

