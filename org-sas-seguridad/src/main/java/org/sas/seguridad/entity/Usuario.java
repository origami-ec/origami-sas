package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"persona"})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String estado;
    private String codigo;
    private String activeDirectory;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaducidad;
    private Boolean caducarClave;
    private Boolean dobleVerificacion;
    @Column(name = "persona")
    private Long personaId;
    private Boolean notificarCorreo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonaFD persona;

    public Usuario() {
    }

    public Usuario(Long id, Boolean dobleVerificacion) {
        this.id = id;
        this.dobleVerificacion = dobleVerificacion;
    }

    public Usuario(Long id, String usuario, Boolean dobleVerificacion, Long personaId) {
        this.id = id;
        this.usuario = usuario;
        this.dobleVerificacion = dobleVerificacion;
        this.personaId = personaId;
    }

    public Usuario(Long id, String usuario, Boolean dobleVerificacion, Long personaId, Boolean notificarCorreo) {
        this.id = id;
        this.usuario = usuario;
        this.dobleVerificacion = dobleVerificacion;
        this.personaId = personaId;
        this.notificarCorreo = notificarCorreo;
    }

    public Usuario(Long id, String usuario, Boolean dobleVerificacion, Long personaId, Boolean notificarCorreo, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.dobleVerificacion = dobleVerificacion;
        this.personaId = personaId;
        this.notificarCorreo = notificarCorreo;
        this.estado = estado;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public Usuario(String usuario) {
        this.usuario = usuario;
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Boolean getDobleVerificacion() {
        return dobleVerificacion;
    }

    public void setDobleVerificacion(Boolean dobleVerificacion) {
        this.dobleVerificacion = dobleVerificacion;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Boolean getCaducarClave() {
        return caducarClave;
    }

    public void setCaducarClave(Boolean caducarClave) {
        this.caducarClave = caducarClave;
    }

    public String getActiveDirectory() {
        return activeDirectory;
    }

    public void setActiveDirectory(String activeDirectory) {
        this.activeDirectory = activeDirectory;
    }

    public PersonaFD getPersona() {
        return persona;
    }

    public void setPersona(PersonaFD persona) {
        this.persona = persona;
    }

    public Boolean getNotificarCorreo() {
        return notificarCorreo;
    }

    public void setNotificarCorreo(Boolean notificarCorreo) {
        this.notificarCorreo = notificarCorreo;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", estado=" + estado +
                ", codigo='" + codigo + '\'' +
                ", fechaCaducidad=" + fechaCaducidad +
                ", caducarClave=" + caducarClave +
                '}';
    }
}
