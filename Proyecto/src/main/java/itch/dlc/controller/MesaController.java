package itch.dlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import itch.dlc.model.Mesa;
import itch.dlc.service.MesaService;

@Controller
@RequestMapping("/mesa")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // Listar mesas
    @GetMapping
    public String listarMesas(Model model) {
        model.addAttribute("mesas", mesaService.listarMesas());
        return "mesa/listaMesas"; // apunta al HTML lista de mesas
    }
    
    @GetMapping("/ver/{id}")
    public String verDetalleMesa(@PathVariable("id") int idMesa, Model model) {
        Mesa mesa = mesaService.obtenerMesaPorId(idMesa);

        if (mesa == null) {
            return "mesa/noEncontrada";
        }

        model.addAttribute("mesa", mesa);
        return "mesa/detalleM";
    }
    // Formulario nueva mesa
    @GetMapping("/nueva")
    public String nuevaMesa(Model model) {
        model.addAttribute("mesa", new Mesa());
        return "mesa/formMesa"; // apunta al HTML formulario mesa
    }

    // Guardar nueva mesa
    @PostMapping("/guardar")
    public String guardarMesa(@ModelAttribute("mesa") Mesa mesa) {
        mesaService.guardarMesa(mesa);
        return "redirect:/mesa";
    }

    // Formulario editar mesa
    @GetMapping("/editar/{id}")
    public String editarMesa(@PathVariable Integer id, Model model) {
        Mesa mesa = mesaService.obtenerMesaPorId(id);
        model.addAttribute("mesa", mesa);
        return "mesa/formMesa";
    }

    // Actualizar mesa
    @PostMapping("/actualizar")
    public String actualizarMesa(@ModelAttribute("mesa") Mesa mesa) {
        mesaService.guardarMesa(mesa); // save funciona para update también
        return "redirect:/mesa";
    }

    // Eliminar mesa
    @GetMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Integer id) {
        mesaService.eliminarMesa(id);
        return "redirect:/mesa";
    }
}