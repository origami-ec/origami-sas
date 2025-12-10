package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "menu_tipo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuTipo  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String identificador;
    private String descripcion;
    @Transient
    private List<Menu> menuListSoyMenubar_byOrden;

    public MenuTipo() {
    }

    public MenuTipo(Integer id) {
        this.id = id;
    }

    public MenuTipo(Integer id, String identificador) {
        this.id = id;
        this.identificador = identificador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Menu> getMenuListSoyMenubar_byOrden() {
        return menuListSoyMenubar_byOrden;
    }

    public void setMenuListSoyMenubar_byOrden(List<Menu> menuListSoyMenubar_byOrden) {
        this.menuListSoyMenubar_byOrden = menuListSoyMenubar_byOrden;
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
        if (!(object instanceof MenuTipo)) {
            return false;
        }
        MenuTipo other = (MenuTipo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "MenuTipo{" +
                "id=" + id +
                ", identificador='" + identificador + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", menuListSoyMenubar_byOrden=" + menuListSoyMenubar_byOrden +
                '}';
    }
}
