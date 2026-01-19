package org.ibarra.dto.sgm;

import java.io.Serializable;
import java.math.BigInteger;

public class CatEnte implements Serializable {

    private static final long serialVersionUID = 8799656478674716638L;

    private BigInteger codUsuario;
    private Long id;

    private String ciRuc;

    private String nombres;

    private String apellidos;

    private String direccion;

    private String estado = "A";
    private String razonSocial;
    private Boolean esPersona = Boolean.TRUE;

    public CatEnte() {
    }

    public CatEnte(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }


    public String getNombres() {
        return nombres;
    }


    public void setNombres(String nombres) {
        this.nombres = nombres;
    }


    public String getApellidos() {
        return apellidos;
    }


    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigInteger getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(BigInteger codUsuario) {
        this.codUsuario = codUsuario;
    }

    /**
     * @return Nombres del ente
     */
    public String getNombreCompleto() {
        if (Boolean.TRUE.equals(esPersona)) {
            return apellidos + " " + nombres;
        } else {
            return razonSocial;
        }
    }

    public String getIdentificacion() {
        return this.ciRuc;
    }

    public CatEnte getPersona() {
        return this;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        //  : Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatEnte)) {
            return false;
        }
        CatEnte other = (CatEnte) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
