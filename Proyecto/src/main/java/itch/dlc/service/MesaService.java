package itch.dlc.service;

import java.util.List;

import itch.dlc.model.Mesa;

public interface MesaService {
	List<Mesa> listarMesas();
    Mesa guardarMesa(Mesa mesa);
    Mesa obtenerMesaPorId(Integer id);
    void eliminarMesa(Integer id);
}
