package itch.dlc.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empleado")
public class Empleado {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    private String nombreCompleto;
    private String puesto;
    private String clave;
    
  public List<Atender> getAtenderList() {
		return atenderList;
	}
	public void setAtenderList(List<Atender> atenderList) {
		this.atenderList = atenderList;
	}
	//Relacion con Empleado
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Atender> atenderList = new ArrayList<>();
    
    public Integer getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
}