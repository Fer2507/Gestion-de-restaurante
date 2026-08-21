package itch.dlc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="Detalle_Pedido")
public class PedidoDetalle {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddetalle;
	
    //Relación con Pedido
    @ManyToOne
    @JoinColumn(name = "idpedido")
    private Pedido pedido;

    //Relación con Producto
    @ManyToOne
    @JoinColumn(name = "idproducto")
    private Producto producto;   
    
    private Integer cantidad;
    private Double precio_unitario;
    private Double subtotal = 0.0;

    // Métodos auxiliares
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (cantidad != null && precio_unitario != null) {
            subtotal = cantidad * precio_unitario;
        }
    }

    public Integer getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(Integer iddetalle) {
        this.iddetalle = iddetalle;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precio_unitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precio_unitario = precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "PedidoDetalle [producto=" + producto.getNombre() +
               ", cantidad=" + cantidad + ", subtotal=" + subtotal + "]";
    }
}