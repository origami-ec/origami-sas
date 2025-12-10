package org.sas.seguridad.dto;

public class UsuarioDetalle {

    private Long id;
    private String usuario;
    private String estado;
    private ServidorDatos servidor;


    public UsuarioDetalle() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public ServidorDatos getServidor() {
        return servidor;
    }

    public void setServidor(ServidorDatos servidor) {
        this.servidor = servidor;
    }

    @Override
    public String toString() {
        return "UsuarioInicioSesion{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
