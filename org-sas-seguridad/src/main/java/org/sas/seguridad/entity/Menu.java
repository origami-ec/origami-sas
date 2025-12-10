package org.sas.seguridad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String nombre;
    private String url;
    private String prettyPattern;
    private String descripcion;
    private Integer numPosicion;
    private String icono;
    private String contexto;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private MenuTipo tipo;
    @JoinColumn(name = "menu_padre_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Menu menuPadre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu", fetch = FetchType.EAGER)
    private Collection<MenuRol> menuRolCollection;
    @Transient
    private Long idMenuPadre;
    @Transient
    private List<Menu> menusHijos_byNumPosicion;

    public Menu() {
    }

    public Menu(MenuTipo menuTipo) {
        this.tipo = tipo;
    }


    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public Menu(Integer id) {
        this.id = id;
    }

    public Menu(Integer id, String nombre, Integer numPosicion) {
        this.id = id;
        this.nombre = nombre;
        this.numPosicion = numPosicion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumPosicion() {
        return numPosicion;
    }

    public void setNumPosicion(Integer numPosicion) {
        this.numPosicion = numPosicion;
    }

    public MenuTipo getTipo() {
        return tipo;
    }

    public void setTipo(MenuTipo tipo) {
        this.tipo = tipo;
    }

    public Collection<MenuRol> getMenuRolCollection() {
        return menuRolCollection;
    }

    public void setMenuRolCollection(Collection<MenuRol> menuRolCollection) {
        this.menuRolCollection = menuRolCollection;
    }

    public Menu getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(Menu menuPadre) {
        this.menuPadre = menuPadre;
    }

    public List<Menu> getMenusHijos_byNumPosicion() {
        return menusHijos_byNumPosicion;
    }

    public void setMenusHijos_byNumPosicion(List<Menu> menusHijos_byNumPosicion) {
        this.menusHijos_byNumPosicion = menusHijos_byNumPosicion;
    }

    public Long getIdMenuPadre() {
        return idMenuPadre;
    }

    public void setIdMenuPadre(Long idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
    }

    public String getPrettyPattern() {
        return prettyPattern;
    }

    public void setPrettyPattern(String prettyPattern) {
        this.prettyPattern = prettyPattern;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", url='" + url + '\'' +
                ", prettyPattern='" + prettyPattern + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", numPosicion=" + numPosicion +
                ", icono='" + icono + '\'' +
                ", contexto='" + contexto + '\'' +
                ", tipo=" + tipo +
                ", menuPadre=" + menuPadre +
                ", idMenuPadre=" + idMenuPadre +
                '}';
    }
}
