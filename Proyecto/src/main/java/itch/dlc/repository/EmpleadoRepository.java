package itch.dlc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.dlc.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
	List<Empleado> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);
	
	Empleado findByNombreCompleto(String nombreCompleto);
}
