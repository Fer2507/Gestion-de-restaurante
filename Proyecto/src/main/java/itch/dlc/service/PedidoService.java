package itch.dlc.service;

import java.util.List;
import itch.dlc.model.Pedido;

public interface PedidoService {
    List<Pedido> buscarTodosPedidos();
    Pedido buscarPorIdPedido(Integer idPedido);
    void guardarPedido(Pedido pedido,  Integer idEmpleado);
    void eliminarPedido(Integer idPedido);
    void actualizarPedido(Pedido pedido, Integer idEmpleado);
}
