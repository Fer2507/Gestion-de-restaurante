package itch.dlc.controller;

import java.beans.PropertyEditorSupport;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import itch.dlc.model.Perfil;
import itch.dlc.model.Usuario;
import itch.dlc.service.PerfilService;
import itch.dlc.service.UsuarioService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Perfil.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    Perfil p = perfilService.buscarPorIdPerfil(Integer.parseInt(text));
                    setValue(p);
                }
            }
        });
    }

    // LISTA
    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuario/listaUsuario";
    }

    // DETALLE
    @GetMapping("/ver/{id}")
    public String verDetalleUsuario(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorIdUsuario(id);
        model.addAttribute("usuario", usuario);
        return "usuario/detalleUsuario";
    }

    // NUEVO
    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfiles", perfilService.buscarTodosPerfil());
        return "usuario/formUsuario";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorIdUsuario(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfiles", perfilService.buscarTodosPerfil());

        return "usuario/formUsuario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarUsuario(
            @ModelAttribute Usuario usuario,
            @RequestParam(name="perfilesSeleccionados", required=false) List<Integer> perfilesIds) {

        usuario.setPerfiles(new LinkedList<>());

        // PERFILES
        if (perfilesIds != null) {
            for (Integer idPerfil : perfilesIds) {
                Perfil p = perfilService.buscarPorIdPerfil(idPerfil);
                usuario.agregarPerfil(p);
            }
        }

        // ----- CONTRASEÑA (con BCrypt) -----
        if (usuario.getIdUsuario() == null) {
            // Nuevo usuario → cifrar la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            // Usuario existente
            Usuario existente = usuarioService.buscarPorIdUsuario(usuario.getIdUsuario());

            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                // No cambió contraseña → conservar la existente
                usuario.setPassword(existente.getPassword());
            } else {
                // Cambió contraseña → cifrar
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        // GUARDAR
        usuarioService.guardarUsuarios(usuario);
        return "redirect:/usuarios/lista";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios/lista";
    }
}