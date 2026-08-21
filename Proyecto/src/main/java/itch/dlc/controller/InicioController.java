package itch.dlc.controller;

import java.util.Date;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import itch.dlc.model.Cliente; 
import itch.dlc.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/clientes")
public class InicioController {

	
	 @Autowired
	    private ClienteService clienteService;
    //Inicio
    @GetMapping("/home")
    public String mostrarInicio(Model model) {
       return "Menu"; 
    }
    //Ver un clinete
    @GetMapping("/cliente")
    public String datosCliente(Model model) {
        String nombre = "Maria Zavaleta";
        String correo = "m.Zavala.h@tecnm.mx";
        double credito = 13000;
        boolean vigente = true;
        Date fechaactual = new Date();

        model.addAttribute("nom", nombre);
        model.addAttribute("correo", correo);
        model.addAttribute("credito", credito);
        model.addAttribute("vigente", vigente);
        model.addAttribute("fecha", fechaactual);

        return "cliente/Cliente";
    }
    //Lista de clientes
    @GetMapping("/listadocli")
    public String mostrarListaClientes(Model model) {
    	List<Cliente> listaClientes = clienteService.buscarTodosClientes();
        model.addAttribute("clientes", listaClientes); 
        return "cliente/listaClientes";
       }
    //Detalle de Cliente por Id
    @GetMapping("/ver/{id}")
    public String verDetalleCliente(@PathVariable("id") int idCliente, Model model) {
        Cliente cliente = clienteService.buscarPorIdCliente(idCliente);

        if (cliente == null) {return "cliente/noEncontrado";}

        model.addAttribute("nombre", cliente.getNombre());
        model.addAttribute("apellidos", cliente.getApellidos());
        model.addAttribute("credito", cliente.getCredito());
        model.addAttribute("telefono", cliente.getTelefono());
        model.addAttribute("email", cliente.getEmail());
        model.addAttribute("destacado", cliente.getDestacado());
        model.addAttribute("fotocliente", cliente.getNombreFoto()); // nombre de la foto
        model.addAttribute("fecha", java.time.LocalDate.now().toString());
        model.addAttribute("clave", cliente.getClave());
        
        return "cliente/detalle";
    }
    
    //Crear Cliente
    @GetMapping("/crear")
    public String crearCliente(Cliente cliente, Model model) {
        model.addAttribute("cliente", new Cliente());
    	/*model.addAttribute("cliente", clienteService.buscarTodosClientes());*/
        return "cliente/formCliente";// buscará formCliente.html en templates/cliente/
    }
  //Guardar Cliente nuevo
    @PostMapping("/guardar")
    public String guardarCliente(
            @ModelAttribute Cliente cliente,
            @RequestParam("fotocliente") MultipartFile foto,
            Model model) {

        try {
            // Subir foto si existe
            if (foto != null && !foto.isEmpty()) {
                String carpetaDestino = "src/main/resources/static/imagen/cliente/";
                Files.createDirectories(Paths.get(carpetaDestino));

                String nombreArchivo = foto.getOriginalFilename();
                Path path = Paths.get(carpetaDestino + nombreArchivo);
                Files.write(path, foto.getBytes());

                cliente.setNombreFoto(nombreArchivo); // Guardar nombre de archivo en DB
            }

            if (cliente.getId() != null) {
                // Modificar cliente existente
                Cliente clienteExistente = clienteService.buscarPorIdCliente(cliente.getId());
                if (clienteExistente != null) {
                    clienteExistente.setNombre(cliente.getNombre());
                    clienteExistente.setApellidos(cliente.getApellidos());
                    clienteExistente.setEmail(cliente.getEmail());
                    clienteExistente.setTelefono(cliente.getTelefono());
                    clienteExistente.setCredito(cliente.getCredito());
                    clienteExistente.setDestacado(cliente.getDestacado());
                    clienteExistente.setClave(cliente.getClave());

                    // Si se subió nueva foto, reemplazar
                    if (cliente.getNombreFoto() != null) {
                        clienteExistente.setNombreFoto(cliente.getNombreFoto());
                    }

                    clienteService.guardarCliente(clienteExistente);
                    System.out.println("Cliente modificado: " + clienteExistente);
                }
            } else {
                // Nuevo cliente
                clienteService.guardarCliente(cliente);
                System.out.println("Cliente nuevo: " + cliente);
            }

        } catch (Exception e) {
            model.addAttribute("msg", "Error al guardar cliente: " + e.getMessage());
            e.printStackTrace();
            return "cliente/formCliente";
        }

        return "redirect:/clientes/listadocli";
    }
    
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") int idCliente, Model model) {
        Cliente cliente = clienteService.buscarPorIdCliente(idCliente);
        if (cliente == null) {
            model.addAttribute("msg", "Cliente no encontrado");
            return "redirect:/cliente/listadocli";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/formCliente"; // se mostrará el formulario con todos los campos llenos
    }
    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") int idCliente) {
        clienteService.eliminarCliente(idCliente);
        return "redirect:/cliente/listadocli";
    }
    
}
