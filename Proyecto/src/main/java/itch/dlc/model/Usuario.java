package itch.dlc.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Usuario")
public class Usuario {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;
	private String nombre;
	private String email;
	private String password;
	private String username;
	private Integer estatus;
	
	@CreationTimestamp
	private Date fechaRegistro;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="UsuarioPerfil", 
	joinColumns = @JoinColumn(name="idusuario"),
	inverseJoinColumns = @JoinColumn(name="idperfil"))
	private List<Perfil> perfiles;

    public void agregarPerfil(Perfil p) {
        if (perfiles == null) {
            perfiles = new LinkedList<>();
        }
        perfiles.add(p);
    }

    // Getters y setters
    public List<Perfil> getPerfiles() { 
    	return perfiles;
    	}
    public void setPerfiles(List<Perfil> perfiles) { 
    	this.perfiles = perfiles; 
    	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
