package itch.dlc.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import itch.dlc.model.Empleado;
import itch.dlc.model.Pedido;
import itch.dlc.model.Reserva;
import itch.dlc.repository.ClienteRepository;
import itch.dlc.repository.EmpleadoRepository;
import itch.dlc.repository.MesaRepository;
import itch.dlc.repository.PedidoRepository;
import itch.dlc.repository.ReservaRepository;

@Controller
@RequestMapping("/buscar")
public class BusquedasController {
	 @Autowired private ReservaRepository reservaRepo;
	    @Autowired private MesaRepository mesaRepo;
	    @Autowired private EmpleadoRepository empleadoRepo;
	    @Autowired private PedidoRepository pedidoRepo;
	    @Autowired private ClienteRepository clienteRepo;

	    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    // ================= RESERVAS =================

	    // Buscar reservas por fecha exacta
	    @PostMapping("/reserva/fechaExacta")
	    public String buscarReservaPorFecha(
	        @RequestParam("fecha") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fecha,
	        Model model
	    ) {
	        LocalDateTime inicio = fecha.atStartOfDay();
	        LocalDateTime fin = fecha.atTime(23, 59, 59);
	        List<Reserva> reservas = reservaRepo.findByFechaExacta(inicio, fin);
	        List<Reserva> pendientes = reservas.stream()
	                .filter(r -> r.getEstatus().equalsIgnoreCase("Pendiente"))
	                .collect(Collectors.toList());

	        List<Reserva> confirmadas = reservas.stream()
	                .filter(r -> r.getEstatus().equalsIgnoreCase("Confirmada"))
	                .collect(Collectors.toList());

	        model.addAttribute("pendientes", pendientes);
	        model.addAttribute("confirmadas", confirmadas);

	    	//System.out.println("Buscando reservas entre " + inicio + " y " + fin);
	        //System.out.println("Resultados encontrados: " + reservas.size());
	        return "reserva/listaReservas";
	    }

	    // Buscar reservas por rango de fechas
	    @PostMapping("/reserva/rango")
	    public String buscarReservaPorRango(
	        @RequestParam("fechaInicio") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate inicio,
	        @RequestParam("fechaFin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fin,
	        Model model
	    ) {
	        LocalDateTime start = inicio.atStartOfDay();
	        LocalDateTime end = fin.plusDays(1).atStartOfDay().minusNanos(1);

	        List<Reserva> reservas = reservaRepo.findByRangoFechas(start, end);
	        
	        List<Reserva> pendientes = reservas.stream()
	                .filter(r -> r.getEstatus().equalsIgnoreCase("Pendiente"))
	                .collect(Collectors.toList());

	        List<Reserva> confirmadas = reservas.stream()
	                .filter(r -> r.getEstatus().equalsIgnoreCase("Confirmada"))
	                .collect(Collectors.toList());

	        model.addAttribute("pendientes", pendientes);
	        model.addAttribute("confirmadas", confirmadas);

	        reservas.forEach(r -> {
	            r.getCliente().getNombre();
	            r.getMesa().getUbicacion();
	        });
	        //System.out.println("Buscando reservas entre " + inicio + " y " + fin);
	        //System.out.println("Resultados encontrados: " + reservas.size());
	        return "reserva/listaReservas";
	    }

	    // Buscar mesas disponibles por capacidad
	    @PostMapping("mesa/capacidad")
	    public String buscarMesasPorCapacidad(@RequestParam("capacidad") int capacidad, Model model) {
	    	 List<Reserva> reservas = reservaRepo.findByMesaCapacidad(capacidad);
	    	    model.addAttribute("pendientes", reservas.stream()
	    	                                  .filter(r -> r.getEstatus().equalsIgnoreCase("Pendiente"))
	    	                                  .toList());
	    	    model.addAttribute("confirmadas", reservas.stream()
	    	                                  .filter(r -> r.getEstatus().equalsIgnoreCase("Confirmada"))
	    	                                  .toList());

	    	    //System.out.println("Buscando reservas con capacidad >= " + capacidad);
	    	    //System.out.println("Resultados encontrados: " + reservas.size());
	    	    return "reserva/formReserva";
	    }

	    // ================= EMPLEADOS =================

	    // Buscar empleados por nombre exacto o por letra
	    @PostMapping("empleado")
	    public String buscarEmpleado(
	        @RequestParam("nombreCompleto") String nombreCompleto,
	        Model model
	    ) {
	        List<Empleado> empleados = empleadoRepo.findByNombreCompletoContainingIgnoreCase(nombreCompleto);
	        model.addAttribute("empleados", empleados);
	        return "empleado/listaEmpleado";
	    }

	    // ================= PEDIDOS =================

	    // Pedidos por fecha exacta
	    @PostMapping("/pedido/fechaExacta")
	    public String buscarPedidoPorFecha(
	        @RequestParam("fecha") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fecha,
	        Model model
	    ) {
	        LocalDateTime inicio = fecha.atStartOfDay();
	        LocalDateTime fin = fecha.atTime(23, 59, 59);
	        List<Pedido> pedidos = pedidoRepo.findByFechaBetween(inicio, fin);
	        model.addAttribute("pedidos", pedidos);
	        model.addAttribute("empleados", empleadoRepo.findAll());
	        model.addAttribute("clientes", clienteRepo.findAll());
	        return "pedido/listaPedidos";
	    }

	    // Pedidos por rango de fechas
	    @PostMapping("pedido/rango")
	    public String buscarPedidoPorRango(
	        @RequestParam("fechaInicio") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate inicio,
	        @RequestParam("fechaFin") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fin,
	        Model model
	    ) {
	        LocalDateTime start = inicio.atStartOfDay();
	        LocalDateTime end = fin.atTime(23, 59, 59);
	        List<Pedido> pedidos = pedidoRepo.findByFechaBetween(start, end);
	        model.addAttribute("pedidos", pedidos);

	        model.addAttribute("empleados", empleadoRepo.findAll());
	        model.addAttribute("clientes", clienteRepo.findAll());
	        return "pedido/ListaPedidos";
	    }

	    // Pedidos por empleado
	    @PostMapping("/pedido/empleado")
	    public String buscarPedidoPorEmpleado(@RequestParam("idEmpleado") Integer idEmpleado, Model model) {
	        List<Pedido> pedidos = pedidoRepo.findByEmpleado(idEmpleado);
	        model.addAttribute("pedidos", pedidos);
	        model.addAttribute("empleados", empleadoRepo.findAll());
	        model.addAttribute("clientes", clienteRepo.findAll());
	        return "pedido/ListaPedidos";
	    }

	    // Pedidos por cliente
	    @PostMapping("/pedido/cliente")
	    public String buscarPedidoPorCliente(@RequestParam("Id") Integer Id, Model model) {
	    	List<Pedido> pedidos = pedidoRepo.findByClientes(Id);
	        model.addAttribute("pedidos", pedidos);
	        model.addAttribute("empleados", empleadoRepo.findAll());
	        model.addAttribute("clientes", clienteRepo.findAll());
	        return "pedido/ListaPedidos";
	    }
}
