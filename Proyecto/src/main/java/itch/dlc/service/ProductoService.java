package itch.dlc.service;

import java.util.List;
import java.util.Optional;

import itch.dlc.model.Producto;

public interface ProductoService {
    List<Producto> buscarTodosProductos();
	
	Producto buscarPorIdProducto(Integer Id);
	
	void guardarProducto(Producto producto);
	
	void eliminarProducto(Integer idProducto);
	
	// Búsquedas personalizadas
    Optional<Producto> buscarNombre(String nombre);
    List<Producto> buscarNombreContiene(String cadena);
    List<Producto> buscarPorTipo(String tipo);
    List<Producto> buscarPorDescripcionContiene(String texto);
    List<Producto> buscarPrecioEntre(Double min, Double max);
    List<Producto> buscarPrecioMayor(Double precio);
    List<Producto> buscarPorTipoYPrecio(String tipo, Double precioMin, Double precioMax);
    List<Producto> buscarFotoNoImagen();
    List<Producto> buscarTop5PorPrecio();

}
