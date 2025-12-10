package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "usuario_verificacion")
public class UsuarioVerificacion  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    private Long codigoVerificacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private Boolean estado;

    public UsuarioVerificacion() {
    }

    public UsuarioVerificacion(Usuario usuario, Long codigoVerificacion, Date fecha, Boolean estado) {
        this.usuario = usuario;
        this.codigoVerificacion = codigoVerificacion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public UsuarioVerificacion(Usuario usuario, Boolean estado) {
        this.usuario = usuario;
        this.estado = estado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(Long codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "UsuarioVerificacion{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", codigoVerificacion=" + codigoVerificacion +
                ", fecha=" + fecha +
                '}';
    }
}
