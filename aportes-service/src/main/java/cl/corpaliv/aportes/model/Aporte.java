package cl.corpaliv.aportes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

/** Aporte de una persona o empresa (equivale a RegistroGuardado de la app). */
@Entity
@Table(name = "aportes")
public class Aporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRegistro tipo;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'APORTE'")
    @Column(nullable = false, length = 20)
    private MotivoRegistro motivo = MotivoRegistro.APORTE;

    @Column(name = "oportunidad_id")
    private Long oportunidadId;

    @Column(nullable = false, length = 200)
    private String oportunidad;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 12)
    private String rut;

    @Column(nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(name = "tipo_aporte", nullable = false, length = 60)
    private String tipoAporte;

    @Column(length = 200)
    private String direccion;

    @Column(length = 120)
    private String giro;

    @Column(length = 150)
    private String representante;

    @Column(name = "rut_representante", length = 12)
    private String rutRepresentante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoContacto estado = EstadoContacto.PENDIENTE;

    @Column(name = "fecha_registro", nullable = false)
    private long fechaRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoRegistro getTipo() { return tipo; }
    public void setTipo(TipoRegistro tipo) { this.tipo = tipo; }

    public MotivoRegistro getMotivo() { return motivo; }
    public void setMotivo(MotivoRegistro motivo) { this.motivo = motivo; }

    public Long getOportunidadId() { return oportunidadId; }
    public void setOportunidadId(Long oportunidadId) { this.oportunidadId = oportunidadId; }

    public String getOportunidad() { return oportunidad; }
    public void setOportunidad(String oportunidad) { this.oportunidad = oportunidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoAporte() { return tipoAporte; }
    public void setTipoAporte(String tipoAporte) { this.tipoAporte = tipoAporte; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getGiro() { return giro; }
    public void setGiro(String giro) { this.giro = giro; }

    public String getRepresentante() { return representante; }
    public void setRepresentante(String representante) { this.representante = representante; }

    public String getRutRepresentante() { return rutRepresentante; }
    public void setRutRepresentante(String rutRepresentante) { this.rutRepresentante = rutRepresentante; }

    public EstadoContacto getEstado() { return estado; }
    public void setEstado(EstadoContacto estado) { this.estado = estado; }

    public long getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(long fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
