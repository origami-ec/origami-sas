package org.ibarra.entity;

import org.ibarra.util.EsquemaConfig;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_tramite", schema = EsquemaConfig.procesos)
public class TipoTramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long unidadAdministrativa;
    private String descripcion;
    private String activitykey;
    private String carpeta;
    private String userDireccion;
    private Boolean estado;
    private String archivoBpmn;
    private String abreviatura;
    private String rolesAcceso;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private Tramite tramite;
    private Boolean ventanillaPublica;

    public TipoTramite() {
    }

    public TipoTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(Long unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(String userDireccion) {
        this.userDireccion = userDireccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getRolesAcceso() {
        return rolesAcceso;
    }

    public void setRolesAcceso(String rolesAcceso) {
        this.rolesAcceso = rolesAcceso;
    }

    public Tramite getTramite() {
        return tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    public Boolean getVentanillaPublica() {
        return ventanillaPublica;
    }

    public void setVentanillaPublica(Boolean ventanillaPublica) {
        this.ventanillaPublica = ventanillaPublica;
    }

    @Override
    public String toString() {
        return "TipoTramite{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", activitykey='" + activitykey + '\'' +
                '}';
    }
}
