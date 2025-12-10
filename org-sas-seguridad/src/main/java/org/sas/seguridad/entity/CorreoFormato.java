package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "correo_formato")
public class CorreoFormato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String asunto;
    private String codigo;
    private String mensaje;
    private String textoVinculo;
    private Boolean estado;

    public CorreoFormato() {
    }

    public CorreoFormato(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTextoVinculo() {
        return textoVinculo;
    }

    public void setTextoVinculo(String textoVinculo) {
        this.textoVinculo = textoVinculo;
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
        if (!(object instanceof CorreoFormato)) {
            return false;
        }
        CorreoFormato other = (CorreoFormato) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.MsgFormatoNotificacion[ id=" + id + " ]";
    }
}
