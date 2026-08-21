package itch.dlc.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import itch.dlc.model.Cliente;
import itch.dlc.model.Reserva;
import itch.dlc.repository.ClienteRepository;
import itch.dlc.repository.MesaRepository;
import itch.dlc.repository.PedidoRepository;
import itch.dlc.service.ReservaService;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired private ReservaService reservarService;

    @Autowired private PedidoRepository pedidoRepository;

    @Autowired private MesaRepository mesaRepository;

    @Autowired private ClienteRepository clienteRepository;

    //Lista de reservas
    @GetMapping("/lista")
    public String listarReservas(Model model) {
        List<Reserva> pendientes = reservarService.listarPorEstatus("Pendiente");
        List<Reserva> confirmadas = reservarService.listarPorEstatus("Confirmada");

        model.addAttribute("pendientes", pendientes);
        model.addAttribute("confirmadas", confirmadas);
        return "reserva/listaReservas";
    }

    //Formulario de nueva reserva
    @GetMapping("/nueva")
    public String nuevaReserva(Model model, @AuthenticationPrincipal User user) {
        Reserva nueva = new Reserva();
        nueva.setEstatus("Pendiente"); //Valor por defecto
        model.addAttribute("reserva", nueva);
        String username = user.getUsername();

        // Verificar si el usuario logueado es un cliente
        Cliente clienteLogueado = clienteRepository.findByEmail(username).orElse(null);
        if (clienteLogueado != null) {
            // Usuario es cliente -> mostrar input readonly y enviar idCliente en hidden
            model.addAttribute("clienteLogueado", clienteLogueado);
        } else {
            // Usuario no es cliente -> mostrar select con todos los clientes
            model.addAttribute("clientes", clienteRepository.findAll());
        }
        model.addAttribute("mesas", mesaRepository.findAll());
        return "reserva/formReserva";
    }

    //Guardar nueva reserva
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttrs) {
        try {
            //Validar cliente
            if (reserva.getCliente() == null || reserva.getCliente().getId() == null) {
                redirectAttrs.addFlashAttribute("error", "Debes seleccionar un cliente.");
                return "redirect:/reserva/nueva";
            }
            reserva.setCliente(clienteRepository.findById(reserva.getCliente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")));

            //Validar mesa
            if (reserva.getMesa() == null || reserva.getMesa().getIdMesa() == null) {
                redirectAttrs.addFlashAttribute("error", "Debes seleccionar una mesa.");
                return "redirect:/reserva/nueva";
            }
            reserva.setMesa(mesaRepository.findById(reserva.getMesa().getIdMesa())
                    .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada")));

            //Estatus por defecto
            reserva.setEstatus("Pendiente");

            reservarService.guardar(reserva);
            redirectAttrs.addFlashAttribute("success", "Reserva guardada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al guardar la reserva.");
            return "redirect:/reserva/nueva";
        }
        return "redirect:/reserva/lista";
    }

    //Ver detalle
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Reserva reserva = reservarService.buscarPorId(id);
        model.addAttribute("reserva", reserva);
        return "reserva/detalleReserva";
    }

    // Confirmar reserva
    @PostMapping("/confirmar/{id}")
    public String confirmarReserva(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
    	 Reserva reserva = reservarService.buscarPorId(id);
    	 
    	    //Fecha actual (solo la fecha, sin hora)
    	    LocalDate hoy = LocalDate.now();

    	    //Obtener solo la fecha de la reserva
    	    LocalDate fechaReserva = reserva.getFechaHora().toLocalDate();

    	    //Validar si la fecha de la reserva ya pasó
    	    if (fechaReserva.isBefore(hoy)) {
    	        redirectAttrs.addFlashAttribute("error",
    	                "No se puede confirmar una reserva cuya fecha ya pasó.");
    	        return "redirect:/reserva/lista";
    	    }

    	    //Si es de hoy o futura -> se confirma
    	    reservarService.cambiarEstatus(id, "Confirmada");

    	    return "redirect:/reserva/lista";
    }
    
    @PostMapping("/pendiente/{id}")
    public String verReservaPendiente(@PathVariable Integer id) {
        reservarService.cambiarEstatus(id, "Pendiente");
        return "redirect:/reserva/lista"; // nombre del template HTML
    }


    // Cancelar reserva (elimina pedidos + la reserva)
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Integer id) {
        Reserva reserva = reservarService.buscarPorId(id);

        if (reserva.getPedidos() != null && !reserva.getPedidos().isEmpty()) {
            pedidoRepository.deleteAll(reserva.getPedidos());
        }

        reservarService.eliminar(id);
        return "redirect:/reserva/lista";
    }

    //Editar reserva
    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Integer id, Model model) {
        Reserva reserva = reservarService.buscarPorId(id);
        if (reserva == null) {
            return "redirect:/reserva/lista";
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("mesas", mesaRepository.findAll());
        return "reserva/formReserva";
    }

    //Actualizar reserva (estatus no se modifica)
    @PostMapping("/actualizar")
    public String actualizarReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttrs) {
        try {
            //Validar si la mesa está ocupada
            boolean ocupada = reservarService.mesaOcupada(
                    reserva.getMesa().getIdMesa(),
                    reserva.getFechaHora(),
                    reserva.getIdReserva()
            );
            if (ocupada) {
                redirectAttrs.addFlashAttribute("error", "La mesa ya está ocupada en la fecha y hora seleccionadas.");
                return "redirect:/reserva/editar/" + reserva.getIdReserva();
            }

            // Recuperar reserva original y mantener estatus
            Reserva reservaOriginal = reservarService.buscarPorId(reserva.getIdReserva());
            if (reservaOriginal != null) {
                reserva.setEstatus(reservaOriginal.getEstatus());
            } else {
                reserva.setEstatus("Pendiente");
            }

            // Recuperar cliente y mesa completos
            if (reserva.getCliente() == null || reserva.getCliente().getId() == null) {
                redirectAttrs.addFlashAttribute("error", "Cliente no válido.");
                return "redirect:/reserva/editar/" + reserva.getIdReserva();
            }
            reserva.setCliente(clienteRepository.findById(reserva.getCliente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")));

            if (reserva.getMesa() == null || reserva.getMesa().getIdMesa() == null) {
                redirectAttrs.addFlashAttribute("error", "Mesa no válida.");
                return "redirect:/reserva/editar/" + reserva.getIdReserva();
            }
            reserva.setMesa(mesaRepository.findById(reserva.getMesa().getIdMesa())
                    .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada")));

            reservarService.guardar(reserva);
            redirectAttrs.addFlashAttribute("success", "Reserva actualizada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al actualizar la reserva.");
            return "redirect:/reserva/editar/" + reserva.getIdReserva();
        }
        return "redirect:/reserva/lista";
    }
    
    @GetMapping("/mis-reservas")
    public String verMisReservas(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepository.findByEmail(user.getUsername())
                             .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        List<Reserva> confirmadas = reservarService.obtenerReservasPorCliente(cliente.getId(), "Confirmada");
        model.addAttribute("confirmadas", confirmadas);
        return "reserva/clientereserva";
    }
}
