package itch.dlc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Mesa")
public class Mesa {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer idMesa;
      private Integer capacidad;
      private String ubicacion;
      
	  public Integer getIdMesa() {
		  return idMesa;
	  }
	  public void setIdMesa(Integer idMesa) {
		  this.idMesa = idMesa;
	  }
	  public Integer getCapacidad() {
		  return capacidad;
	  }
	  public void setCapacidad(Integer capacidad) {
		  this.capacidad = capacidad;
	  }
	  public String getUbicacion() {
		  return ubicacion;
	  }
	  public void setUbicacion(String ubicacion) {
		  this.ubicacion = ubicacion;
	  }
      
}
