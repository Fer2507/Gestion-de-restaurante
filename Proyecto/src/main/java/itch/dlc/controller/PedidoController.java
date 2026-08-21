package itch.dlc.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import itch.dlc.model.Atender;
import itch.dlc.model.Cliente;
import itch.dlc.model.Empleado;
import itch.dlc.model.Pedido;
import itch.dlc.model.Reserva;
import itch.dlc.repository.*;
import itch.dlc.service.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired private PedidoService pedidoService;
    @Autowired private ClienteService clienteService;
    @Autowired private ProductoService productoService;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private EmpleadoRepository empleadoRepository;
    @Autowired private AtenderRepository atenderRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private ReservaService reservaService;

    // Listar pedidos
    @GetMapping("/lista")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.buscarTodosPedidos());
        model.addAttribute("empleados", empleadoRepository.findAll());
        model.addAttribute("clientes", clienteService.buscarTodosClientes());
        return "pedido/ListaPedidos";
    }

    // Mostrar formulario de creación sin reserva
    @GetMapping("/crear")
    public String crearPedido(Model model, @AuthenticationPrincipal User user) {
        Pedido pedido = new Pedido();
        pedido.setReserva(null);
        model.addAttribute("pedido", pedido);
        model.addAttribute("clientes", clienteService.buscarTodosClientes());
        model.addAttribute("productos", productoService.buscarTodosProductos());

     // Usuario logueado
        String username = user.getUsername();

        // Verificar si es ADMIN o SUPERVISOR
        boolean esAdminOSupervisor = user.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMINISTRADOR") ||
                               r.getAuthority().equals("SUPERVISOR"));

        if (esAdminOSupervisor) {

            // Admin o supervisor puede elegir cualquier empleado
            model.addAttribute("empleados", empleadoRepository.findAll());

        } else {

        	Empleado empleado = empleadoRepository.findByNombreCompleto(username);
            // Enviar empleado único al formulario
            model.addAttribute("empleado", empleado);
        }
        model.addAttribute("idReserva", 0);
                return "pedido/nuevoPedido";
    }


    // Crear pedido asociado a una reserva
    @GetMapping("/nuevo/{idReserva}")
    public String nuevoPedidoConReserva(@PathVariable Integer idReserva, Model model) {
        Pedido pedido = new Pedido();
        Reserva reserva = reservaRepository.findById(idReserva).orElse(null);
        pedido.setReserva(reserva);

        if (reserva != null && reserva.getCliente() != null) {
            pedido.setCliente(reserva.getCliente());
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("clientes", clienteService.buscarTodosClientes());
        model.addAttribute("empleados", empleadoRepository.findAll());
        model.addAttribute("idReserva", reserva != null ? reserva.getIdReserva() : "null");
        return "reserva/nuevoPedidoReserva";
    }

    // Guardar pedido (con o sin reserva)
    @PostMapping("/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido,
                                @RequestParam(required = false) String idReserva,
                                @RequestParam("idEmpleado") Integer idEmpleado,
                                RedirectAttributes redirectAttrs) {

    	try {
            // Verificar si tiene o no reserva
            if (idReserva == null || idReserva.equals("0")) {
                pedido.setReserva(null);
            } else {
                Reserva reserva = reservaService.buscarPorId(Integer.parseInt(idReserva));
                pedido.setReserva(reserva);
            }

            // Guardar pedido
            pedidoService.guardarPedido(pedido, idEmpleado);

            // Redirigir según corresponda
            if (pedido.getReserva() != null) {
                redirectAttrs.addFlashAttribute("success", "Pedido guardado correctamente con reserva.");
                return "redirect:/reserva/lista";
            } else {
                redirectAttrs.addFlashAttribute("success", "Pedido guardado correctamente.");
                return "redirect:/pedidos/lista";
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al guardar el pedido.");
            // En caso de error, redirige según contexto
            if (idReserva != null && !idReserva.equals("0")) {
                return "redirect:/reserva/lista";
            } else {
                return "redirect:/pedidos/lista";
            }
        }
    }


    // Ver detalles de un pedido
    @GetMapping("/detalles/{idPedido}")
    public String verDetalles(@PathVariable("idPedido") Integer idPedido, Model model) {
        Pedido pedido = pedidoService.buscarPorIdPedido(idPedido);
        if (pedido == null) return "redirect:/pedidos/lista";

        Atender atender = atenderRepository.findAll().stream()
                .filter(a -> a.getPedido().getIdpedido().equals(idPedido))
                .findFirst().orElse(null);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", pedido.getDetalles());
        model.addAttribute("empleado", atender != null ? atender.getEmpleado() : null);
        return "pedido/detallepedido";
    }

    // Eliminar pedido
    @GetMapping("/eliminar/{idPedido}")
    public String eliminar(@PathVariable("idPedido") Integer idPedido) {
        pedidoService.eliminarPedido(idPedido);
        return "redirect:/pedidos/lista";
    }

    // Editar pedido
    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable("id") Integer id, Model model) {
        Pedido pedido = pedidoService.buscarPorIdPedido(id);

        // Si no tiene fecha, asignamos la fecha/hora actual
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        }

        Atender atender = atenderRepository.findAll().stream()
                .filter(a -> a.getPedido().getIdpedido().equals(id))
                .findFirst().orElse(null);

        model.addAttribute("pedido", pedido);
        model.addAttribute("clientes", clienteService.buscarTodosClientes());
        model.addAttribute("productos", productoService.buscarTodosProductos());
        model.addAttribute("empleados", empleadoRepository.findAll());
        model.addAttribute("empleadoAsignado", atender != null ? atender.getEmpleado() : null);
        model.addAttribute("idReserva", pedido.getReserva() != null ? pedido.getReserva().getIdReserva() : 0);
        return "pedido/editarPedido";
    }
    
    @GetMapping("/editar/reserva/{idPedido}")
    public String editarPedidoReserva(@PathVariable Integer idPedido, Model model) {
        Pedido pedido = pedidoService.buscarPorIdPedido(idPedido);
        if (pedido == null) return "redirect:/pedidos/lista";

        // Si no tiene fecha, asignamos la fecha/hora actual
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        }

        Atender atender = atenderRepository.findAll().stream()
                .filter(a -> a.getPedido().getIdpedido().equals(idPedido))
                .findFirst().orElse(null);

        model.addAttribute("pedido", pedido);
        model.addAttribute("clientes", clienteService.buscarTodosClientes());
        model.addAttribute("productos", productoService.buscarTodosProductos());
        model.addAttribute("empleados", empleadoRepository.findAll());
        model.addAttribute("empleadoAsignado", atender != null ? atender.getEmpleado() : null);
        model.addAttribute("idReserva", pedido.getReserva() != null ? pedido.getReserva().getIdReserva() : 0);

        return "reserva/editarPedidoReserva"; // Vista específica para pedidos con reserva
    }


    // ✅ Actualizar pedido
    @PostMapping("/actualizar")
    public String actualizarPedido(@ModelAttribute Pedido pedido,
                                   @RequestParam("idEmpleado") Integer idEmpleado) {

        if (pedido.getDetalles() != null) {
            pedido.getDetalles().forEach(d -> d.setPedido(pedido));
            pedido.getDetalles().removeIf(d -> d.getProducto() == null || d.getProducto().getIdProducto() == null);
        }

        // Mantener o asignar fecha correctamente
        if (pedido.getFecha() == null) {
            Pedido pedidoExistente = pedidoService.buscarPorIdPedido(pedido.getIdpedido());
            pedido.setFecha(pedidoExistente != null ? pedidoExistente.getFecha() : LocalDateTime.now());
        }

        pedido.calcularTotal();
        pedidoService.actualizarPedido(pedido, idEmpleado);

        // Guardar o actualizar la relación con el empleado
        Atender atender = atenderRepository.findAll().stream()
                .filter(a -> a.getPedido().getIdpedido().equals(pedido.getIdpedido()))
                .findFirst().orElse(new Atender());

        atender.setEmpleado(empleadoRepository.findById(idEmpleado).orElseThrow());
        atender.setPedido(pedido);
        atenderRepository.save(atender);

        return "redirect:/pedidos/lista";
    }
    
    @GetMapping("/mis-pedidos")
    public String misPedidos(@AuthenticationPrincipal User user, Model model) {
    	String emailUsuario = user.getUsername();

        Cliente cliente = clienteRepo.findByEmail(emailUsuario)
                .orElse(null);

        if (cliente == null) {
            model.addAttribute("error", "No se encontró el cliente asociado al usuario.");
            return "pedido/clientePedidos";
        }

        List<Pedido> pedidos = pedidoRepository.findByClienteId(cliente.getId());

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("cliente", cliente);
        model.addAttribute("empleados", empleadoRepository.findAll());

        return "pedido/clientePedidos";
    }

    @GetMapping("/mis-pedidos-empleado")
    public String misPedidosEmpleado(@AuthenticationPrincipal User user, Model model) {

        String nombreEmpleado = user.getUsername();

        Empleado empleado = empleadoRepository.findByNombreCompleto(nombreEmpleado);

        if (empleado == null) {
            model.addAttribute("error", "No se encontró un empleado asociado al usuario.");
            return "pedido/empleadoPedidos";
        }

        List<Atender> atenciones = atenderRepository.findByEmpleadoIdEmpleado(empleado.getIdEmpleado());

        List<Pedido> pedidos = atenciones.stream()
                .map(Atender::getPedido)
                .toList();

        model.addAttribute("empleado", empleado);
        model.addAttribute("pedidos", pedidos);

        return "pedido/empleadoPedidos";
    }
}
