package itch.dlc.service.jpa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itch.dlc.model.Empleado;
import itch.dlc.model.Perfil;
import itch.dlc.model.Usuario;
import itch.dlc.repository.EmpleadoRepository;
import itch.dlc.repository.PerfilRepository;
import itch.dlc.repository.UsuarioRepository;
import itch.dlc.service.EmpleadoService;

@Service
public class EmpleadoServiceJpa implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepo;
    @Autowired
	private UsuarioRepository usuarioRepo;
    @Autowired
    private PerfilRepository perfilRepo;

    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepo.findAll();
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
    	Empleado savedEmpleado = empleadoRepo.save(empleado);
    	
    	Usuario usuario = new Usuario();
	 	    usuario.setUsername(empleado.getNombreCompleto());
	 	    usuario.setPassword("{noop}" + empleado.getClave());
	 	    usuario.setNombre(empleado.getNombreCompleto());
	 	    usuario.setEmail(empleado.getNombreCompleto().replaceAll(" ", ".") + "@empleado.com");
	 	    usuario.setEstatus(1);
	 	   String rol = switch (empleado.getPuesto()) {
	        case "Mesero" -> "MESERO";
	        case "Cocinero" -> "COCINERO";
	        case "Cajero" -> "CAJERO";
	        case "Supervisor" -> "SUPERVISOR";
	        default -> "EMPLEADO";
		    };
	
		    Perfil perfil = perfilRepo.findByPerfil(rol)
		    	    .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + rol));

		    	// Asignar al usuario
		    	List<Perfil> perfiles = new ArrayList<>();
		    	perfiles.add(perfil);
		    	usuario.setPerfiles(perfiles);
		    	usuarioRepo.save(usuario);
	 	    // Guardar usuario
	 	    usuarioRepo.save(usuario);
	 	   return savedEmpleado;
    }

    @Override
    public Empleado obtenerEmpleadoPorId(Integer id) {
        return empleadoRepo.findById(id).orElse(null);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        empleadoRepo.deleteById(id);
    }
}
