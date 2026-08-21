package itch.dlc.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import itch.dlc.model.Producto;
import itch.dlc.repository.ProductoRepository;
import itch.dlc.service.ProductoService;

@Service
@Primary
public class ProductoServiceJpa implements ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Override
    public List<Producto> buscarTodosProductos() {
        return productoRepo.findAll();
    }

    @Override
    public Producto buscarPorIdProducto(Integer idProducto) {
        Optional<Producto> optional = productoRepo.findById(idProducto);
        return optional.orElse(null);
    }

    @Override
    public void guardarProducto(Producto producto) {
        productoRepo.save(producto);
    }

    @Override
    public void eliminarProducto(Integer idProducto) {
        productoRepo.deleteById(idProducto);
    }
    
    //Métodos de búsqueda

    @Override
    public Optional<Producto> buscarNombre(String nombre) {
        return productoRepo.findByNombre(nombre);
    }

    @Override
    public List<Producto> buscarNombreContiene(String cadena) {
        return productoRepo.findByNombreContaining(cadena);
    }

    @Override
    public List<Producto> buscarPorTipo(String tipo) {
        return productoRepo.findByTipo(tipo);
    }

    @Override
    public List<Producto> buscarPrecioEntre(Double min, Double max) {
        return productoRepo.findByPrecioBetween(min, max);
    }

    @Override
    public List<Producto> buscarPrecioMayor(Double precio) {
        return productoRepo.findByPrecioGreaterThan(precio);
    }
    @Override
    public List<Producto> buscarFotoNoImagen() {
        return productoRepo.findByNombreFoto();
    }

    @Override
    public List<Producto> buscarTop5PorPrecio() {
        return productoRepo.findTop5ByOrderByPrecioDesc();
    }

    @Override
    public List<Producto> buscarPorDescripcionContiene(String texto) {
        return productoRepo.findByDescripcionContaining(texto);
    }

    @Override
    public List<Producto> buscarPorTipoYPrecio(String tipo, Double precioMin, Double precioMax) {
        return productoRepo.findByTipoAndPrecioBetween(tipo, precioMin, precioMax);
    }
}
