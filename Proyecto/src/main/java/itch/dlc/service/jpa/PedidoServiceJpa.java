package itch.dlc.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

import itch.dlc.model.Pedido;
import itch.dlc.model.PedidoDetalle;
import itch.dlc.model.Producto;
import itch.dlc.model.Empleado;
import itch.dlc.model.Atender;
import itch.dlc.repository.PedidoRepository;
import itch.dlc.repository.ProductoRepository;
import itch.dlc.repository.ReservaRepository;
import itch.dlc.repository.EmpleadoRepository;
import itch.dlc.repository.AtenderRepository;
import itch.dlc.service.PedidoService;

@Primary
@Service
public class PedidoServiceJpa implements PedidoService {

    @Autowired private PedidoRepository pedidoRepo;

    @Autowired private ProductoRepository productoRepo;

    @Autowired private EmpleadoRepository empleadoRepo;

    @Autowired private AtenderRepository atenderRepo;
    
    @Autowired private ReservaRepository reservaRepo;

    @Override
    public List<Pedido> buscarTodosPedidos() {
        return pedidoRepo.findAll();
    }

    @Override
    public Pedido buscarPorIdPedido(Integer idPedido) {
        return pedidoRepo.findById(idPedido).orElse(null);
    }

    @Override
    public void guardarPedido(Pedido pedido, Integer idEmpleado) {
        List<PedidoDetalle> detalles = pedido.getDetalles();

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }

        for (PedidoDetalle detalle : detalles) {
            if (detalle.getProducto() == null || detalle.getProducto().getIdProducto() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener un producto válido");
            }

            // Cargar producto real desde la DB
            Producto productoReal = productoRepo.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado para el id " + detalle.getProducto().getIdProducto()));

            detalle.setProducto(productoReal); // reemplazar con objeto real
            detalle.setPedido(pedido);
            detalle.calcularSubtotal();
        }

        // Calcular total
        pedido.calcularTotal();

        // Guardar pedido primero
        pedidoRepo.save(pedido);

        // Crear la relación Atender
        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        Atender atender = new Atender();
        atender.setEmpleado(empleado);
        atender.setPedido(pedido);
        atenderRepo.save(atender);
    }

    @Override
    public void eliminarPedido(Integer idPedido) {
        pedidoRepo.deleteById(idPedido);
    }

    @Override
    public void actualizarPedido(Pedido pedido, Integer idEmpleado) {
        if (pedido.getDetalles() != null) {
            pedido.getDetalles().forEach(d -> d.setPedido(pedido));
        }

        pedido.getDetalles().removeIf(d -> d.getProducto() == null || d.getProducto().getIdProducto() == null);

        // Validar la reserva solo si existe un ID válido
        if (pedido.getReserva() != null && pedido.getReserva().getIdReserva() != null && pedido.getReserva().getIdReserva() != 0) {
            Integer reservaId = pedido.getReserva().getIdReserva();
            // Buscar la reserva en la DB
            reservaRepo.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con id " + reservaId));
        } else {
            // Si no hay reserva, se establece como null
            pedido.setReserva(null);
        }

        pedido.calcularTotal();
        pedidoRepo.save(pedido);

        // Actualizar o crear relación Atender
        Atender atender = atenderRepo.findAll().stream()
                .filter(a -> a.getPedido().getIdpedido().equals(pedido.getIdpedido()))
                .findFirst()
                .orElse(new Atender());

        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        atender.setEmpleado(empleado);
        atender.setPedido(pedido);
        atenderRepo.save(atender);
    }
}
