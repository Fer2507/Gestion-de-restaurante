package itch.dlc.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import itch.dlc.model.Cliente;
import itch.dlc.model.Perfil;
import itch.dlc.model.Usuario;
import itch.dlc.repository.ClienteRepository;
import itch.dlc.repository.PerfilRepository;
import itch.dlc.repository.UsuarioRepository;
import itch.dlc.service.ClienteService;

@Service
@Primary
public class ClienteServiceJpa implements ClienteService{
	@Autowired
	private ClienteRepository clienteRepo;
	@Autowired
	private PerfilRepository perfilRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Override
	public List<Cliente> buscarTodosClientes(){
		return clienteRepo.findAll();
	}
	@Override
	public Cliente buscarPorIdCliente(Integer IdCliente) {
		Optional<Cliente> optional=clienteRepo.findById(IdCliente);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	//Guardara un cliente en la lista
	public void guardarCliente(Cliente cliente) {

	    // Guardar cliente
	    Cliente savedCliente = clienteRepo.save(cliente);

	    // Buscar si existe un usuario con ese email
	    Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(cliente.getEmail());

	    if (usuarioOpt.isPresent()) {
	        //Ya existe → solo actualiza
	        Usuario usuario = usuarioOpt.get();
	        usuario.setUsername(cliente.getEmail());
	        usuario.setPassword(passwordEncoder.encode(cliente.getClave()));
		    usuario.setNombre(cliente.getNombre());
		    usuario.setEmail(cliente.getEmail());
		    usuario.setEstatus(1);
	        usuarioRepo.save(usuario);
	        return; // salir, no crear usuario nuevo
	    }

	    //Si NO existe → lo creas (cliente nuevo)
	    Usuario usuario = new Usuario();
	    usuario.setUsername(cliente.getEmail());
	    usuario.setPassword(passwordEncoder.encode(cliente.getClave()));
	    usuario.setNombre(cliente.getNombre());
	    usuario.setEmail(cliente.getEmail());
	    usuario.setEstatus(1);

	    Usuario savedUsuario = usuarioRepo.save(usuario);

	    // Asignar perfil una sola vez
	    Perfil clientePerfil = perfilRepo.findByPerfil("CLIENTE")
	        .orElseThrow(() -> new RuntimeException("Perfil CLIENTE no existe"));

	    usuarioRepo.asignarPerfil(savedUsuario.getIdUsuario(), clientePerfil.getIdPerfil());
	}
	@Override
    public void eliminarCliente(Integer id) {
		clienteRepo.deleteById(id);
	}
	
	//BUSQUEDAS
	@Override
    public Optional<Cliente> buscarNombre(String nombre) {
        return clienteRepo.findByNombre(nombre);
    }

    @Override
    public List<Cliente> buscarNombreContiene(String cadena) {
        return clienteRepo.findByNombreContaining(cadena);
    }

    @Override
    public Optional<Cliente> buscarEmail(String email) {
        return clienteRepo.findByEmail(email);
    }

    @Override
    public List<Cliente> buscarEmailGmail() {
        return clienteRepo.findByEmailEndingWith("@gmail.com");
    }

    @Override
    public List<Cliente> buscarCreditoEntre(Double min, Double max) {
        return clienteRepo.findByCreditoBetween(min, max);
    }

    @Override
    public List<Cliente> buscarCreditoMayor(Double monto) {
        return clienteRepo.findByCreditoGreaterThan(monto);
    }

    @Override
    public List<Cliente> buscarDestacados() {
        return clienteRepo.findByDestacado(1);
    }

    @Override
    public List<Cliente> buscarNombreYCredito(String nombre, Double credito) {
        return clienteRepo.findByNombreContainingAndCreditoGreaterThan(nombre, credito);
    }

    @Override
    public List<Cliente> buscarFotoNoImagen() {
        return clienteRepo.findByNombreFoto("no_imagen.jpg");
    }

    @Override
    public List<Cliente> buscarDestacadosCreditoMayor(Double credito) {
        return clienteRepo.findByDestacadoAndCreditoGreaterThan(1, credito);
    }

    @Override
    public List<Cliente> buscarTop5Credito() {
        return clienteRepo.findTop5ByOrderByCreditoDesc();
    }
    
    @Override
    public Optional<Cliente> buscarPorIdUsuario(Integer idUsuario) {
        return clienteRepo.findByUsuario_IdUsuario(idUsuario);
    }

}
