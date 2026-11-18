package cl.corpaliv.oportunidades.config;

import cl.corpaliv.oportunidades.model.Oportunidad;
import cl.corpaliv.oportunidades.repository.OportunidadRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Carga las oportunidades iniciales (las mismas del respaldo local de la app) si la tabla está vacía. */
@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner cargarOportunidades(OportunidadRepository repositorio) {
        return args -> {
            if (repositorio.count() > 0) {
                return;
            }
            repositorio.saveAll(List.of(
                    new Oportunidad(
                            "Taller de Preparación Laboral Alba Lab",
                            "RSE Empresarial",
                            "Programa de capacitación en oficios adaptados para jóvenes con discapacidad múltiple.",
                            "12 participantes capacitados para vinculación laboral este año.",
                            "https://images.unsplash.com/photo-1529070538774-1843cb3265df?q=80&w=600",
                            "rse@corpaliv-demo.cl",
                            5_000_000, 1_200_000),
                    new Oportunidad(
                            "Equipamiento Escuela Jean Van Dijk",
                            "Donación Directa",
                            "Fondo destinado a la adquisición de material didáctico adaptado y sillas ergométricas.",
                            "Beneficia directamente a 30 estudiantes en su desarrollo autónomo.",
                            "https://plus.unsplash.com/premium_photo-1661380797814-d0bcc01342b7?q=80&w=600",
                            null,
                            3_000_000, 250_000)));
        };
    }
}
