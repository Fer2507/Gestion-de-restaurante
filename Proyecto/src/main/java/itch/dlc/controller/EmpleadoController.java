package itch.dlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

import itch.dlc.model.Empleado;
import itch.dlc.repository.EmpleadoRepository;
import itch.dlc.service.EmpleadoService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoService empleadoService;

    EmpleadoController(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    // Listar empleados
    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        return "empleado/listaEmpleado"; // apunta al HTML lista de empleados
    }
    
    @GetMapping("/ver/{id}")
    public String verDetalleEmpleado(@PathVariable("id") int idEmpleado, Model model) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(idEmpleado);

        if (empleado == null) {
            return "empleado/noEncontrado"; // Página de error si no existe
        }

        // PASAMOS EL OBJETO COMPLETO
        model.addAttribute("empleado", empleado);

        return "empleado/detalleEmpl";
    }

    // Formulario nuevo empleado
    @GetMapping("/nuevo")
    public String nuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "empleado/formEmpleado"; // apunta al HTML formulario empleado
    }

    // Guardar nuevo empleado
    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.guardarEmpleado(empleado);
        return "redirect:/empleado";
    }

    // Formulario editar empleado
    @GetMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id, Model model) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        model.addAttribute("empleado", empleado);
        return "empleado/formEmpleado";
    }

    // Actualizar empleado
    @PostMapping("/actualizar")
    public String actualizarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.guardarEmpleado(empleado); // save funciona para update también
        return "redirect:/empleado";
    }

    // Eliminar empleado
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
        return "redirect:/empleado";
    }
}
