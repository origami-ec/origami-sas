package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "usuario_clave")
public class UsuarioClave  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuario;
    private String clave;
    private Boolean estado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public UsuarioClave() {
    }

    public UsuarioClave(Long id, Long usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "UsuarioClave{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", clave=" + clave +
                ", fecha=" + fecha +
                '}';
    }
}
