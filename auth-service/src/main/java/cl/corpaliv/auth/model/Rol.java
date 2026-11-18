package cl.corpaliv.auth.model;

/** Roles de la cuenta (el rol se muestra como badge bajo el nombre en el perfil de la app). */
public enum Rol {
    ADMINISTRADOR("Administrador");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
