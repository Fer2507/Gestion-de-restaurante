package itch.dlc.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Configuracion implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/productos/**") //Acceder a los archivos.
                .addResourceLocations("file:///C:/Users/ferna/Pictures/Productos/"); //Ruta física en tu disco
    }
}