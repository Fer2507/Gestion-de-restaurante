package itch.dlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import itch.dlc.model.Pedido;
import itch.dlc.service.PedidoService;
import itch.dlc.service.jpa.TicketService;

@Controller
@RequestMapping("/ticket")
public class TicketController {
	 @Autowired
	 
	    private TicketService ticketService;

	    @Autowired
	    private PedidoService pedidoService;

	    @GetMapping("/imprimir/{id}")
	    public ResponseEntity<ByteArrayResource> imprimirTicket(@PathVariable Integer id) throws Exception {
	        Pedido pedido = pedidoService.buscarPorIdPedido(id);

	        byte[] pdf = ticketService.generarTicket(pedido);
	        ByteArrayResource resource = new ByteArrayResource(pdf);

	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + id + ".pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .contentLength(pdf.length)
	            .body(resource);
	    }
}
