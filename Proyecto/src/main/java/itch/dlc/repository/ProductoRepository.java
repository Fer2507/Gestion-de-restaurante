package itch.dlc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import itch.dlc.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByNombre(String nombre);

    List<Producto> findByNombreContaining(String cadena);

    List<Producto> findByTipo(String tipo);
    
    @Query("SELECT p FROM Producto p WHERE p.nombreFoto IS NULL OR p.nombreFoto = ''")
    List<Producto> findByNombreFoto();

    List<Producto> findByDescripcionContaining(String texto);

    List<Producto> findByPrecioBetween(Double min, Double max);

    List<Producto> findByPrecioGreaterThan(Double precio);

    List<Producto> findByTipoAndPrecioBetween(String tipo, Double precioMin, Double precioMax);

    List<Producto> findTop5ByOrderByPrecioDesc();

}
