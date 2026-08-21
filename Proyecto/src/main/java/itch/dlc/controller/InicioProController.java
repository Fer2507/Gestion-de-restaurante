package itch.dlc.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import itch.dlc.model.Producto;
import itch.dlc.service.ProductoService;
import itch.dlc.service.jpa.ProductoServiceJpa;

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
@RequestMapping("/")
public class InicioProController {
	
	private  ProductoServiceJpa productoServiceJpa;
	@Autowired
	private  ProductoService productoService;
	
	
	
    @GetMapping("/producto/producto")
    public String datosproducto(Model model) {
        String nombre = "Medallones de Papa a la Jardinera";
    	String descripcion = "Es simplemente deliciosa";
    	String tipo = "Platillo";
    	String fotoproducto = "medallon.jpg";
    	double precio = 1000;
    	
    	model.addAttribute("nom", nombre);
    	model.addAttribute("fotoProducto", fotoproducto);
    	model.addAttribute("tipo", tipo);
    	model.addAttribute("descripcion", descripcion);
    	model.addAttribute("precio",precio);

        return "producto/producto";

    }

    @GetMapping("/producto/listadopro")
    public String mostrarListaProductos(Model model) {

    	List<Producto> listaProducto = productoService.buscarTodosProductos();
        model.addAttribute("productos", listaProducto); 
        return "producto/listaProductos";
       }
    
    @GetMapping()
    public String mostrarListaProductosCli(Model model) {

    	List<Producto> listaProducto = productoService.buscarTodosProductos();
        model.addAttribute("productos", listaProducto); 
        return "Inicio";
       }
    
    @GetMapping("/producto/ver/{id}")
    public String verDetalleProducto(@PathVariable("id") int idProducto,@RequestParam(required = false) String from, Model model) {
        Producto producto = productoService.buscarPorIdProducto(idProducto);
        if (producto == null) {return "producto/noEncontrado";}

        model.addAttribute("nombreProducto", producto.getNombre());
        model.addAttribute("descripcionProducto", producto.getDescripcion());
        model.addAttribute("tipoProducto", producto.getTipo());
        model.addAttribute("fotoProducto", producto.getNombreFoto());
        model.addAttribute("precioProducto", producto.getPrecio());
        model.addAttribute("from", from); 

        return "producto/detallepro";
    }
    
    
 // Mostrar formulario para dar de alta un producto
    @GetMapping("/producto/alta")
    public String mostrarFormulario(Model model) {
        model.addAttribute("producto", new Producto()); // Objeto vacío para el formulario
        return "producto/altaproducto";
    } 

    // Guardar o actualizar producto
    @PostMapping("/producto/guardar")
    public String guardarProducto(
            @ModelAttribute Producto producto,
            @RequestParam("foto") MultipartFile foto,
            Model model) {

        try {
            if (foto != null && !foto.isEmpty()) {
                String carpetaDestino = "C:/Users/ferna/Pictures/Productos/";
                Path rutaCarpeta = Paths.get(carpetaDestino);
                Files.createDirectories(rutaCarpeta);

                String nombreArchivo = foto.getOriginalFilename();
                Path rutaArchivo = rutaCarpeta.resolve(nombreArchivo);
                Files.write(rutaArchivo, foto.getBytes());

                producto.setNombreFoto(nombreArchivo);
            }

            if (producto.getIdProducto() != null) {
                // Modificar producto existente
                Producto existente = productoService.buscarPorIdProducto(producto.getIdProducto());
                if (existente != null) {
                    existente.setNombre(producto.getNombre());
                    existente.setDescripcion(producto.getDescripcion());
                    existente.setTipo(producto.getTipo());
                    existente.setPrecio(producto.getPrecio());
                    if (producto.getNombreFoto() != null) {
                        existente.setNombreFoto(producto.getNombreFoto());
                    }
                    productoService.guardarProducto(existente);
                }
            } else {
                // Nuevo producto
                productoService.guardarProducto(producto);
            }

        } catch (Exception e) {
            model.addAttribute("msg", "Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            return "producto/altaproducto";
        }

        return "redirect:/producto/listadopro";
    }


    // Editar producto (mostrar formulario con datos llenos)
    @GetMapping("/producto/editar/{id}")
    public String editarProducto(@PathVariable("id") int idProducto, Model model) {
        Producto producto = productoService.buscarPorIdProducto(idProducto);
        if (producto == null) {
            model.addAttribute("msg", "Producto no encontrado");
            return "redirect:/producto/listadopro";
        }
        model.addAttribute("producto", producto);
        return "producto/altaproducto"; // se mostrará el formulario con todos los campos llenos
    }

    // Eliminar producto (con confirmación en HTML)
    @GetMapping("/producto/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") int idProducto) {
        productoService.eliminarProducto(idProducto);
        return "redirect:/producto/listadopro";
    }
    
}
