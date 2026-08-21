package itch.dlc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import itch.dlc.model.Cliente;
import itch.dlc.service.ClienteService;

@Controller
@RequestMapping("/busquedas")
public class ClienteBusquedaController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String mostrarFormulario() {
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/porNombre")
    public String buscarPorNombre(@RequestParam String nombre, Model model) {
        Optional<Cliente> cliente = clienteService.buscarNombre(nombre);
        model.addAttribute("clientes", cliente.map(List::of).orElse(List.of()));
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/porNombreContiene")
    public String buscarPorNombreContiene(@RequestParam String cadena, Model model) {
        model.addAttribute("clientes", clienteService.buscarNombreContiene(cadena));
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/porEmail")
    public String buscarPorEmail(@RequestParam String email, Model model) {
        Optional<Cliente> cliente = clienteService.buscarEmail(email);
        model.addAttribute("clientes", cliente.map(List::of).orElse(List.of()));
        return "cliente/ClienteBusqueda";
    }

    @GetMapping("/gmail")
    public String buscarGmail(Model model) {
        model.addAttribute("clientes", clienteService.buscarEmailGmail());
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/entreCreditos")
    public String buscarEntreCreditos(@RequestParam Double min, @RequestParam Double max, Model model) {
        model.addAttribute("clientes", clienteService.buscarCreditoEntre(min, max));
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/mayorCredito")
    public String buscarMayorCredito(@RequestParam Double monto, Model model) {
        model.addAttribute("clientes", clienteService.buscarCreditoMayor(monto));
        return "cliente/ClienteBusqueda";
    }

    @GetMapping("/destacados")
    public String buscarDestacados(Model model) {
        model.addAttribute("clientes", clienteService.buscarDestacados());
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/nombreCredito")
    public String buscarPorNombreYCredito(@RequestParam String nombre, @RequestParam Double credito, Model model) {
        model.addAttribute("clientes", clienteService.buscarNombreYCredito(nombre, credito));
        return "cliente/ClienteBusqueda";
    }

    @GetMapping("/fotoNoImagen")
    public String buscarFotoNoImagen(Model model) {
        model.addAttribute("clientes", clienteService.buscarFotoNoImagen());
        return "cliente/ClienteBusqueda";
    }

    @PostMapping("/destacadosCredito")
    public String buscarDestacadosCredito(@RequestParam Double credito, Model model) {
        model.addAttribute("clientes", clienteService.buscarDestacadosCreditoMayor(credito));
        return "cliente/ClienteBusqueda";
    }

    @GetMapping("/top5")
    public String buscarTop5(Model model) {
        model.addAttribute("clientes", clienteService.buscarTop5Credito());
        return "cliente/ClienteBusqueda";
    }
}
