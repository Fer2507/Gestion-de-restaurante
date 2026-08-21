package itch.dlc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import itch.dlc.model.Producto;
import itch.dlc.service.ProductoService;

@Controller
@RequestMapping("/busquedaPro")
public class ProductoBusquedaController {

    @Autowired
    private ProductoService productoService;

    // FORMULARIO GENERAL DE BÚSQUEDA
    @GetMapping("")
    public String mostrarFormulario() {
        return "producto/ProductoBusqueda";
    }

    // MÉTODOS DE BÚSQUEDA
    @PostMapping("/porNombrePro")
    public String buscarPorNombre(@RequestParam String nombre,
                                  @RequestParam(required = false) String origen,
                                  Model model) {
        Optional<Producto> producto = productoService.buscarNombre(nombre);
        model.addAttribute("productos", producto.map(List::of).orElse(List.of()));
        return elegirVista(origen);
    }

    @PostMapping("/porNombreContienePro")
    public String buscarPorNombreContiene(@RequestParam String cadena,
                                          @RequestParam(required = false) String origen,
                                          Model model) {
        model.addAttribute("productos", productoService.buscarNombreContiene(cadena));
        return elegirVista(origen);
    }

    @PostMapping("/porTipoPro")
    public String buscarPorTipo(@RequestParam String tipo,
                                @RequestParam(required = false) String origen,
                                Model model) {
        model.addAttribute("productos", productoService.buscarPorTipo(tipo));
        return elegirVista(origen);
    }

    @PostMapping("/porDescripcionPro")
    public String buscarPorDescripcion(@RequestParam String texto,
                                       @RequestParam(required = false) String origen,
                                       Model model) {
        model.addAttribute("productos", productoService.buscarPorDescripcionContiene(texto));
        return elegirVista(origen);
    }

    @PostMapping("/entrePreciosPro")
    public String buscarEntrePrecios(@RequestParam Double min,
                                     @RequestParam Double max,
                                     @RequestParam(required = false) String origen,
                                     Model model) {
        model.addAttribute("productos", productoService.buscarPrecioEntre(min, max));
        return elegirVista(origen);
    }

    @PostMapping("/porPrecioMayorPro")
    public String buscarPorPrecioMayor(@RequestParam Double precio,
                                       @RequestParam(required = false) String origen,
                                       Model model) {
        model.addAttribute("productos", productoService.buscarPrecioMayor(precio));
        return elegirVista(origen);
    }

    @PostMapping("/porTipoYPrecioPro")
    public String buscarPorTipoYPrecio(@RequestParam String tipo,
                                       @RequestParam Double precioMin,
                                       @RequestParam Double precioMax,
                                       @RequestParam(required = false) String origen,
                                       Model model) {
        model.addAttribute("productos", productoService.buscarPorTipoYPrecio(tipo, precioMin, precioMax));
        return elegirVista(origen);
    }

    @GetMapping("/top5Pro")
    public String buscarTop5(@RequestParam(required = false) String origen, Model model) {
        model.addAttribute("productos", productoService.buscarTop5PorPrecio());
        return elegirVista(origen);
    }

    @GetMapping("/sinImagenPro")
    public String buscarSinImagen(@RequestParam(required = false) String origen, Model model) {
        model.addAttribute("productos", productoService.buscarFotoNoImagen());
        return elegirVista(origen);
    }

    // MÉTODO AUXILIAR
    private String elegirVista(String origen) {
        if ("inicio".equalsIgnoreCase(origen)) {
            // Si la búsqueda viene del inicio (productos.html)
            return "Inicio";  // tu lista principal
        } else {
            // Si viene desde el formulario de búsqueda (ProductoBusqueda.html)
            return "producto/listaProductos";
        }
    }
}
