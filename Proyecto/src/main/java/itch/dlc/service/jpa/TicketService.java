package itch.dlc.service.jpa;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import itch.dlc.model.Pedido;

@Service
public class TicketService {
	  public byte[] generarTicket(Pedido pedido) throws Exception {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Document doc = new Document(PageSize.A6); // tamaño tipo ticket
	        PdfWriter.getInstance(doc, baos);

	        doc.open();

	        // Encabezado
	        Paragraph titulo = new Paragraph("RESTAURANTE - TICKET\n\n", new Font(Font.HELVETICA, 14, Font.BOLD));
	        titulo.setAlignment(Element.ALIGN_CENTER);
	        doc.add(titulo);

	        doc.add(new Paragraph("Fecha: " + pedido.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
	        doc.add(new Paragraph("Cliente: " + pedido.getCliente().getNombre()));
	        String mesero = pedido.getAtenciones().isEmpty() ? "N/A" : pedido.getAtenciones().get(0).getEmpleado().getNombreCompleto();
	        doc.add(new Paragraph("Mesero: " + mesero ));
	        doc.add(new Paragraph("----------------------------------------"));

	        // Tabla productos
	        PdfPTable table = new PdfPTable(3);
	        table.addCell("Producto");
	        table.addCell("Cant");
	        table.addCell("Total");

	        pedido.getDetalles().forEach(det -> {
	            table.addCell(det.getProducto().getNombre());
	            table.addCell(String.valueOf(det.getCantidad()));
	            table.addCell("$" + det.getSubtotal());
	        });

	        doc.add(table);
	        doc.add(new Paragraph("----------------------------------------"));
	        doc.add(new Paragraph("TOTAL: $" + pedido.getTotal() + "\n\n", new Font(Font.HELVETICA, 12, Font.BOLD)));

	        doc.add(new Paragraph("¡Gracias por su compra!", new Font(Font.HELVETICA, 10, Font.ITALIC)));
	        doc.close();

	        return baos.toByteArray();
	    }
	}