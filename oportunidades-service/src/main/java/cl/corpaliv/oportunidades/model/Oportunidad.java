package cl.corpaliv.oportunidades.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Programa o proyecto de CORPALIV al que se puede aportar (equivale a OportunidadModel de la app). */
@Entity
@Table(name = "oportunidades")
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "impacto_social", nullable = false, length = 500)
    private String impactoSocial;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "contacto_email", length = 150)
    private String contactoEmail;

    @Column(name = "meta_financiamiento", nullable = false)
    private int metaFinanciamiento;

    @Column(name = "monto_recaudado", nullable = false)
    private int montoRecaudado;

    public Oportunidad() {
    }

    public Oportunidad(String titulo, String categoria, String descripcion, String impactoSocial,
                       String imagenUrl, String contactoEmail, int metaFinanciamiento, int montoRecaudado) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.impactoSocial = impactoSocial;
        this.imagenUrl = imagenUrl;
        this.contactoEmail = contactoEmail;
        this.metaFinanciamiento = metaFinanciamiento;
        this.montoRecaudado = montoRecaudado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImpactoSocial() { return impactoSocial; }
    public void setImpactoSocial(String impactoSocial) { this.impactoSocial = impactoSocial; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getContactoEmail() { return contactoEmail; }
    public void setContactoEmail(String contactoEmail) { this.contactoEmail = contactoEmail; }

    public int getMetaFinanciamiento() { return metaFinanciamiento; }
    public void setMetaFinanciamiento(int metaFinanciamiento) { this.metaFinanciamiento = metaFinanciamiento; }

    public int getMontoRecaudado() { return montoRecaudado; }
    public void setMontoRecaudado(int montoRecaudado) { this.montoRecaudado = montoRecaudado; }
}
