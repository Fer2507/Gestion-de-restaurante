package itch.dlc.security;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    @Bean
    public UserDetailsManager usuarios(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        users.setUsersByUsernameQuery(
        	    "select username, password, estatus as enabled " +
        	    "from usuario where username = ?"
        	);


        users.setAuthoritiesByUsernameQuery(
        	    "select u.username, p.perfil as authority " +
        	    "from UsuarioPerfil up " +
        	    "inner join Usuario u on u.idUsuario = up.idUsuario " +
        	    "inner join Perfil p on p.idPerfil = up.idPerfil " +
        	    "where u.username = ?"
        	);
        
        return users;
    }

    @Bean
    public SecurityFilterChain filtrosUsuarios(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
            // LOGIN & ESTÁTICOS
            .requestMatchers("/login", "/logout", "/css/**", "/img/**",
                             "/bootstrap/**", "/images/**", "/tinymce/**", "/productos/**").permitAll()
            // PÚBLICAS
            .requestMatchers("/","/producto/ver/**", "/busquedaPro/**" ).permitAll()
            
             // USUARIOS
            .requestMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")
            .requestMatchers("/perfiles/**").hasAnyAuthority("ADMINISTRADOR")
            
            // CLIENTES
            .requestMatchers("/clientes/**").hasAnyAuthority("CAJERO","ADMINISTRADOR", "MESERO", "SUPERVISOR")
            
            // PRODUCTOS
            .requestMatchers("/producto/**", "/busquedaPro/**").hasAnyAuthority("ADMINISTRADOR","SUPERVISOR")

            // EMPLEADOS
            .requestMatchers("/empleado/**").hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
            //MESAS
            .requestMatchers("/mesa/**").hasAnyAuthority("ADMINISTRADOR","SUPERVISOR")

            // PEDIDOS
            .requestMatchers("/pedidos/mis-pedidos").hasAnyAuthority("CLIENTE","ADMINISTRADOR")
            .requestMatchers("/pedidos/mis-pedidos-empleado").hasAnyAuthority("MESERO", "ADMINISTRADOR")
            .requestMatchers("/pedidos/lista").hasAnyAuthority("COCINERO","ADMINISTRADOR", "CAJERO","MESERO")
            .requestMatchers("/pedidos/detalles/**").hasAnyAuthority("MESERO","COCINERO","ADMINISTRADOR", "CLIENTE", "CAJERO")
            .requestMatchers("/pedidos/**").hasAnyAuthority("CAJERO","MESERO","ADMINISTRADOR")

            // RESERVACIONES
            .requestMatchers("/reserva/mis-reservas").hasAuthority("CLIENTE")
            .requestMatchers("/reserva/nueva", "/reserva/guardar").hasAnyAuthority("CLIENTE","ADMINISTRADOR","CAJERO")
            .requestMatchers("/reserva/detalle/**").hasAnyAuthority("CLIENTE","ADMINISTRADOR","CAJERO")
            .requestMatchers("/reserva/**").hasAnyAuthority("CAJERO","ADMINISTRADOR")
            
            //TICKETS
            .requestMatchers("/ticket/**").hasAnyAuthority("ADMINISTRADOR","CAJERO","SUPERVISOR" )

            // BUSQUEDAS
            .requestMatchers("/buscar/pedido/**").hasAnyAuthority("MESERO","ADMINISTRADOR","CAJERO","SUPERVISOR")
            .requestMatchers( "/busquedaPro/**").hasAnyAuthority("MESERO","ADMINISTRADOR" ,"CAJERO","SUPERVISOR")
            .requestMatchers("/busquedas/**").hasAnyAuthority("MESERO", "ADMINISTRADOR","CAJERO" ,"SUPERVISOR")

            // CUALQUIER OTRA REQUIERE LOGIN
            .anyRequest().authenticated()
        );

        // LOGIN
        http.formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        );

        // LOGOUT
        http.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/logout")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

        // PÁGINA DE ACCESO DENEGADO
        http.exceptionHandling(ex -> ex
            .accessDeniedPage("/acceso-denegado")
        );

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}