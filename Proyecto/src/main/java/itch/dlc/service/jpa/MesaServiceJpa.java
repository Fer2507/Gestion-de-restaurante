package itch.dlc.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itch.dlc.model.Mesa;
import itch.dlc.repository.MesaRepository;
import itch.dlc.service.MesaService;

@Service
public class MesaServiceJpa implements MesaService {

    @Autowired
    private MesaRepository mesaRepo;

    @Override
    public List<Mesa> listarMesas() {
        return mesaRepo.findAll();
    }

    @Override
    public Mesa guardarMesa(Mesa mesa) {
        return mesaRepo.save(mesa);
    }

    @Override
    public Mesa obtenerMesaPorId(Integer id) {
        return mesaRepo.findById(id).orElse(null);
    }

    @Override
    public void eliminarMesa(Integer id) {
        mesaRepo.deleteById(id);
    }
}
