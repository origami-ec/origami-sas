package org.ibarra.dto.sgm;

import java.io.Serializable;

public class AclUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String usuario;
    private String usuarioSigm;
    private String pass;
    private Boolean sisEnabled;
    private Boolean userIsDirector;
    private String rutaImagen;

    private String imagenPerfil;

    private CatEnte ente;
    private Boolean esUrbano = Boolean.TRUE;
    private Boolean esSuperUser = Boolean.FALSE;
    private Boolean caducarClave = Boolean.TRUE;
    private String formPrinc;

    public AclUser() {
    }

    public AclUser(Long id) {
        this.id = id;
    }

    public AclUser(Long id, String usuario, Boolean sisEnabled) {
        this.id = id;
        this.usuario = usuario;
        this.sisEnabled = sisEnabled;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public Boolean getUserIsDirector() {
        return userIsDirector;
    }

    public void setUserIsDirector(Boolean userIsDirector) {
        this.userIsDirector = userIsDirector;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public Boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(Boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    public Boolean getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public String getUsuarioSigm() {
        return usuarioSigm;
    }

    public void setUsuarioSigm(String usuarioSigm) {
        this.usuarioSigm = usuarioSigm;
    }

    public Boolean getEstado() {
        return this.sisEnabled;
    }

    public void setEstado(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public Boolean getCaducarClave() {
        return caducarClave;
    }

    public void setCaducarClave(Boolean caducarClave) {
        this.caducarClave = caducarClave;
    }

    public String getFormPrinc() {
        return formPrinc;
    }

    public void setFormPrinc(String formPrinc) {
        this.formPrinc = formPrinc;
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
        if (!(object instanceof AclUser)) {
            return false;
        }
        AclUser other = (AclUser) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
