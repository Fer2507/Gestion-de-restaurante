package itch.dlc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.dlc.model.Atender;

public interface AtenderRepository extends JpaRepository<Atender, Integer> {
	List<Atender> findByEmpleadoIdEmpleado(Integer idEmpleado);
}
