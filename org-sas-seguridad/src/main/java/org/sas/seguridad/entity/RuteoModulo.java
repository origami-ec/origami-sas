package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ruteo_modulo")
public class RuteoModulo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private String hostApp;
    private String appName;
    private Long entidad;
    private Boolean estado;
    private String descripcion;
    private String icon;

    public RuteoModulo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostApp() {
        return hostApp;
    }

    public void setHostApp(String hostApp) {
        this.hostApp = hostApp;
    }

    public String getAppName() {
        return appName.replace("**", "");
    }


    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getEntidad() {
        return entidad;
    }

    public void setEntidad(Long entidad) {
        this.entidad = entidad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        if (!(object instanceof RuteoModulo)) {
            return false;
        }
        RuteoModulo other = (RuteoModulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RuteoModulo{" +
                "id=" + id +
                ", hostApp='" + hostApp + '\'' +
                ", appName='" + appName + '\'' +
                ", entidad=" + entidad +
                ", estado=" + estado +
                '}';
    }
}
