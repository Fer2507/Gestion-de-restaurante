package itch.dlc.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idpedido;

    //Relación con Cliente (muchos pedidos pertenecen a un cliente)
    @ManyToOne
    @JoinColumn(name = "idcliente")
    private Cliente cliente;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime fecha;

    private Double total = 0.0;
    
    public List<Atender> getAtenciones() {
		return atenciones;
	}

	public void setAtenciones(List<Atender> atenciones) {
		this.atenciones = atenciones;
	}

	//Relación con los detalles del pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> detalles = new ArrayList<>();
    
    //Relacion con Atender
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Atender> atenciones = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "idReserva")
    private Reserva reserva;
    
    @Column(name = "estatus")
    private String estatus = "Pendiente";
    
    public Reserva getReserva() {
		return reserva;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public void calcularTotal() {
        if (detalles != null && !detalles.isEmpty()) {
            this.total = detalles.stream()
            		             .filter(d -> d != null && d.getSubtotal() != null)
                                 .mapToDouble(PedidoDetalle::getSubtotal)
                                 .sum();
        } else {
            this.total = 0.0;
        }
    }
   
    public Integer getIdpedido() {
    	return idpedido; 
    	}

	public void setIdpedido(Integer idpedido) { 
		this.idpedido = idpedido; 
		}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	 public List<PedidoDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<PedidoDetalle> detalles) {
		this.detalles = detalles;
	}
	
	@PrePersist
	public void prePersist() {
	    if (fecha == null) {
	        fecha = LocalDateTime.now();
	    }
	}


	 @Override
	    public String toString() {
	        return "Pedido [idPedido=" + idpedido + ", cliente=" + cliente.getNombre() +
	               ", fecha=" + fecha + ", total=" + total + "]";
	    }
}
