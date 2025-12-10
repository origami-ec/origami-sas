package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario_rol")
public class UsuarioRol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "rol", referencedColumnName = "id")
    @ManyToOne
    private Rol rol;

    @Transient
    private Boolean eliminado;
    @Transient
    private Integer cantidad = 0;

    public UsuarioRol() {
    }

    public UsuarioRol(Long id) {
        this.id = id;
    }

    public UsuarioRol(Rol rol, Usuario usuario) {
        this.rol = rol;
        this.usuario = usuario;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "UsuarioRol{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", rol=" + rol +
                ", eliminado=" + eliminado +
                ", cantidad=" + cantidad +
                '}';
    }
}
