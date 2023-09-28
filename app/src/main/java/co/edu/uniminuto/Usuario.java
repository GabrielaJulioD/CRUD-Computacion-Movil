package co.edu.uniminuto;

public class Usuario {
    private int documento;
    private String usuario;
    private String nombre;
    private String apellidos;
    private String contra;

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "documento=" + documento +
                ", usuario='" + usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", contra='" + contra + '\'' +
                '}';
    }
    public String getFormattedDetails() {
        return "Documento: " + documento +
                "\nUsuario: " + usuario +
                "\nNombres: " + nombre +
                "\nApellidos: " + apellidos;
    }


}
