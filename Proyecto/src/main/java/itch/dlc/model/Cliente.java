package itch.dlc.model;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "Cliente")
public class Cliente {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer Id;
	
	private String nombre;
	private String apellidos;
	private String email;
	private Double credito;
	private String telefono;
	private Integer destacado;
	private String nombreFoto;
	private String clave;
	
	@ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    // getters y setters
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	// Para recibir el archivo desde el formulario
	@Transient
	private MultipartFile fotocliente;
	
	//Conexion
	 @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	    private List<Pedido> pedidos;
	
	public String getNombreFoto() {
		return nombreFoto;
	}
	public void setNombreFoto(String nombreFoto) {
		this.nombreFoto = nombreFoto;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getCredito() {
		return credito;
	}
	public void setCredito(double credito) {
		this.credito = credito;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Integer getDestacado() {
		return destacado;
	}
	public void setDestacado(Integer destacado) {
		this.destacado = destacado;
	}
	public MultipartFile getFotocliente() {
		return fotocliente;
	}
	public void setFotocliente(MultipartFile fotocliente) {
		this.fotocliente = fotocliente;
	}
	
	@Override
	public String toString() {
		return "Cliente [Id=" + Id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email
				+ ", credito=" + credito + ", telefono=" + telefono + ", destacado=" + destacado + ", fotocliente="
				+ fotocliente + ", clave=" + clave +"]";
	}
	
 
}