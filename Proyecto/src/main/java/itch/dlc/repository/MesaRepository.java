package itch.dlc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.dlc.model.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {
	List<Mesa> findByCapacidadGreaterThanEqual(int capacidad);
}
