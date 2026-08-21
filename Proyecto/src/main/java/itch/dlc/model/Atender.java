package itch.dlc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Atender")
public class Atender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtender;

    @ManyToOne
    @JoinColumn(name = "idEmpleado", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "idPedido", nullable = false)
    private Pedido pedido;
    
    // Getters y Setters
    public Integer getIdAtender() { return idAtender; }
    public void setIdAtender(Integer idAtender) { this.idAtender = idAtender; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}
