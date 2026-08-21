package itch.dlc.model;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Producto")
public class Producto {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer idProducto;
	
	private String nombre;
	private String descripcion;
	private Double precio;
	private String tipo;
	private String nombreFoto;
	
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PedidoDetalle> detalles;
	
	public String getNombreFoto() {
		return nombreFoto;
	}
	public Integer getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	public List<PedidoDetalle> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<PedidoDetalle> detalles) {
		this.detalles = detalles;
	}
	public void setNombreFoto(String nombreFoto) {
		this.nombreFoto = nombreFoto;
	}
	// Para recibir el archivo desde el formulario
	public MultipartFile getFotoproducto() {
		return fotoproducto;
	}
	public void setFotoproducto(MultipartFile fotoproducto) {
		this.fotoproducto = fotoproducto;
	}
	@Transient
	private MultipartFile fotoproducto;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
