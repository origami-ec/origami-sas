package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.sas.seguridad.dto.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "menu_rol")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuRol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "menu", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Menu menu;
    private Long rol;
    @Transient
    private List<Document> documents;
    @Transient
    private String permiso;

    public MenuRol() {
    }

    public MenuRol(Menu menu) {
        this.menu = menu;
    }

    public MenuRol(Long id) {
        this.id = id;
    }

    public MenuRol(Long id, Long rol) {
        this.id = id;
        this.rol = rol;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getRol() {
        return rol;
    }

    public void setRol(Long rol) {
        this.rol = rol;
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
        if (!(object instanceof MenuRol)) {
            return false;
        }
        MenuRol other = (MenuRol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "MenuRol{" +
                "id=" + id +
                ", menu=" + menu +
                ", rol=" + rol +
                ", documents=" + documents +
                ", permiso='" + permiso + '\'' +
                '}';
    }
}
