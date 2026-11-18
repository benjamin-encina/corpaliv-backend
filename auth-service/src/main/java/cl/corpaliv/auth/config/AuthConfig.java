package cl.corpaliv.auth.config;

import cl.corpaliv.auth.model.Rol;
import cl.corpaliv.auth.model.Usuario;
import cl.corpaliv.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Crea la cuenta de administrador de demostración si todavía no existe. */
    @Bean
    public CommandLineRunner crearAdministrador(UsuarioRepository repositorio,
                                                PasswordEncoder encoder,
                                                @Value("${admin.nombre}") String nombre,
                                                @Value("${admin.correo}") String correo,
                                                @Value("${admin.clave}") String clave) {
        return args -> {
            if (repositorio.findByCorreoIgnoreCase(correo).isEmpty()) {
                repositorio.save(new Usuario(nombre, correo, encoder.encode(clave), Rol.ADMINISTRADOR));
            }
        };
    }
}
