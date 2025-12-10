package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "entidad")
public class Entidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private String nombreEntidad;
    private String abreviatura;
    private String ruc;
    private String codigoMef;
    private String codigoUnidadEjecutora;
    private String direccion;
    private Short numero;
    @Column(name = "telefono_1")
    private String telefono1;
    @Column(name = "telefono_2")
    private String telefono2;
    private String movil;
    private String email;
    private Boolean estado;
    @Column(name = "canton")
    private Long canton;

    @Transient
    private String urlLogoCompleta;

    public Entidad() {
        this.estado = Boolean.TRUE;
    }

    public Entidad(Long id) {
        this.id = id;
    }

    public Entidad(Long id, String nombreEntidad, String abreviatura, String ruc, String codigoMef,
                   String codigoUnidadEjecutora, String direccion, String telefono1, String email, Boolean estado) {
        this.id = id;
        this.nombreEntidad = nombreEntidad;
        this.abreviatura = abreviatura;
        this.ruc = ruc;
        this.codigoMef = codigoMef;
        this.codigoUnidadEjecutora = codigoUnidadEjecutora;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.email = email;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCodigoMef() {
        return codigoMef;
    }

    public void setCodigoMef(String codigoMef) {
        this.codigoMef = codigoMef;
    }

    public String getCodigoUnidadEjecutora() {
        return codigoUnidadEjecutora;
    }

    public void setCodigoUnidadEjecutora(String codigoUnidadEjecutora) {
        this.codigoUnidadEjecutora = codigoUnidadEjecutora;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Short getNumero() {
        return numero;
    }

    public void setNumero(Short numero) {
        this.numero = numero;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Long getCanton() {
        return canton;
    }

    public void setCanton(Long canton) {
        this.canton = canton;
    }

    public String getUrlLogoCompleta() {
        return urlLogoCompleta;
    }

    public void setUrlLogoCompleta(String urlLogoCompleta) {
        this.urlLogoCompleta = urlLogoCompleta;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof Entidad)) {
            return false;
        }
        Entidad other = (Entidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.DatosGeneralesEntidad[ id=" + id + " ]";
    }
}
