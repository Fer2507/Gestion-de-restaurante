package itch.dlc.service;
import java.util.List;
import java.util.Optional;

import itch.dlc.model.Cliente;

public interface ClienteService {
	//Listar Clientes
	List<Cliente> buscarTodosClientes();
	//Busacr un cliente por el id
	Cliente buscarPorIdCliente(Integer IdCliente);
	//Guardara un cliente en la lista
	void guardarCliente(Cliente cliente);
	
	//Eliminar
	void eliminarCliente(Integer id);
	
	//BUSQUEDAS
	//Nombre exacto
	 Optional<Cliente> buscarNombre(String nombre);
	 //Nombre por Cadena
	 List<Cliente> buscarNombreContiene(String cadena);
	 //Email exacto
	 Optional<Cliente> buscarEmail(String email);
	 //Email con @gmail.com
	 List<Cliente> buscarEmailGmail();
	 //Credito entre dos valores
	 List<Cliente> buscarCreditoEntre(Double min, Double max);
	 //Credito Mayor a
	 List<Cliente> buscarCreditoMayor(Double monto);
	 //Destacado
	 List<Cliente> buscarDestacados();
	 //Credito y Nombre
	 List<Cliente> buscarNombreYCredito(String nombre, Double credito);
	 //"no_imagen.jpg"
	 List<Cliente> buscarFotoNoImagen();
	 //Destacado y Credito mayor a
	 List<Cliente> buscarDestacadosCreditoMayor(Double credito);
	 //5 clientes con mayor cedito
	 List<Cliente> buscarTop5Credito();
	 
	 Optional<Cliente> buscarPorIdUsuario(Integer idUsuario);

}
