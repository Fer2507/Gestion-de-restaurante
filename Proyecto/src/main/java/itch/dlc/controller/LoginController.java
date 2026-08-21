package itch.dlc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	 @GetMapping("/login")
	    public String login() {
	        return "Login";
	    }
	 
	@GetMapping("/logout")
	    public String logout() {
	        return "Logout";
	    }
	 @GetMapping("/acceso-denegado")
	    public String accesoDenegado() {
	        return "denegado"; // nombre del html sin extensión
	    }
}
