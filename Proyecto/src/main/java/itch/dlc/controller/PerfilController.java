package itch.dlc.controller;

import itch.dlc.model.Perfil;
import itch.dlc.service.PerfilService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;
 
    // LISTA
    @GetMapping("/lista")
    public String listarPerfiles(Model model) {
        model.addAttribute("perfiles", perfilService.buscarTodosPerfil());
        return "perfil/listPerfil";
    }

    // DETALLE
    @GetMapping("/ver/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Perfil perfil = perfilService.buscarPorIdPerfil(id);

        model.addAttribute("perfil", perfil);
        return "perfil/detallePerfil";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String nuevoPerfil(Model model) {
        model.addAttribute("perfil", new Perfil());
        return "perfil/formPerfil";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarPerfil(@PathVariable Integer id, Model model) {
        Perfil perfil = perfilService.buscarPorIdPerfil(id);
        model.addAttribute("perfil", perfil);

        return "perfil/formPerfil";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarPerfil(@ModelAttribute("perfil") Perfil perfil) {
    	perfilService.guardarPerfil(perfil);
        return "redirect:/perfiles/lista";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarPerfil(@PathVariable Integer id) {
        perfilService.eliminarPerfil(id);
        return "redirect:/perfiles/lista";
    }

}
