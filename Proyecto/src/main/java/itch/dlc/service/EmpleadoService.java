package itch.dlc.service;

import java.util.List;

import itch.dlc.model.Empleado;

public interface EmpleadoService {
	  List<Empleado> listarEmpleados();
	  Empleado guardarEmpleado(Empleado empleado);
	  Empleado obtenerEmpleadoPorId(Integer id);
      void eliminarEmpleado(Integer id);
}
