package org.sas.zull.entity;


import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    @Formula(" (SELECT uc.clave FROM usuario_clave uc WHERE uc.usuario = id AND uc.estado = true ORDER BY uc.id DESC LIMIT 1) ")
    private String clave;
    private String codigo;
    private String imagen;
    private String estado;
    private String activeDirectory;
    private Boolean caducarClave;
    private Date fechaCaducidad;
    private Long persona;
    private Boolean notificarCorreo;

    public Usuario() {
    }

    public Usuario(String usuario) {
        this.usuario = usuario;
    }

    public Usuario(String usuario, String clave, String estado) {
        this.usuario = usuario;
        this.clave = clave;
        this.estado = estado;
    }

    public Usuario(String usuario, String estado) {
        this.usuario = usuario;
        this.estado = estado;
    }

    public Usuario(Long id, String usuario, String estado, String activeDirectory ) {
        this.id = id;
        this.usuario = usuario;
        this.estado = estado;
        this.activeDirectory = activeDirectory;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getCaducarClave() {
        return caducarClave;
    }

    public void setCaducarClave(Boolean caducarClave) {
        this.caducarClave = caducarClave;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getActiveDirectory() {
        return activeDirectory;
    }

    public void setActiveDirectory(String activeDirectory) {
        this.activeDirectory = activeDirectory;
    }

    public Boolean getNotificarCorreo() {
        return notificarCorreo;
    }

    public void setNotificarCorreo(Boolean notificarCorreo) {
        this.notificarCorreo = notificarCorreo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", codigo='" + codigo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", estado=" + estado +
                ", caducarClave=" + caducarClave +
                ", fechaCaducidad=" + fechaCaducidad +
                ", persona=" + persona +
                '}';
    }
}
