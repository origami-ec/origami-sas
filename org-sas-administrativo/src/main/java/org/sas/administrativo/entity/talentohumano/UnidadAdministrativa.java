package org.sas.administrativo.entity.talentohumano;



import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "unidad_administrativa", schema = EsquemaConfig.talentoHumano)
public class UnidadAdministrativa implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "padre", referencedColumnName = "id")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UnidadAdministrativa padre;
    private String codigo;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    private CatalogoItem tipo;
    private Date vigencia;
    private Date caducidad;
    private String estado;
    private String urlImagen;
    private String ubicacion;
    /*@ManyToOne
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    private CatalogoItem nivel;
    @ManyToOne
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    private Servidor responsable;
    @ManyToOne
    @JoinColumn(name = "director", referencedColumnName = "id")
    private Servidor director;*/
    private String userCreacion;
    private Date fechaCreacion;
    private String userModificacion;
    private Date fechaModificacion;

    public UnidadAdministrativa() {
    }

    public UnidadAdministrativa(Long id) {
        this.id = id;
    }

    public UnidadAdministrativa(UnidadAdministrativa padre, String nombre, CatalogoItem tipo) {
        this.padre = padre;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public UnidadAdministrativa(String nombre, CatalogoItem tipo, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    public UnidadAdministrativa(UnidadAdministrativa padre, String codigo, String nombre, CatalogoItem tipo, Date vigencia, Date caducidad, String estado, String urlImagen, String ubicacion) {
        this.padre = padre;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vigencia = vigencia;
        this.caducidad = caducidad;
        this.estado = estado;
        this.urlImagen = urlImagen;
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadAdministrativa getPadre() {
        return padre;
    }

    public void setPadre(UnidadAdministrativa padre) {
        this.padre = padre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CatalogoItem getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItem tipo) {
        this.tipo = tipo;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

  /*  public CatalogoItem getNivel() {
        return nivel;
    }

    public void setNivel(CatalogoItem nivel) {
        this.nivel = nivel;
    }

    public Servidor getResponsable() {
        return responsable;
    }

    public void setResponsable(Servidor responsable) {
        this.responsable = responsable;
    }

    public Servidor getDirector() {
        return director;
    }

    public void setDirector(Servidor director) {
        this.director = director;
    }*/

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UnidadAdministrativa{" +
                "id=" + id +
                ", padre=" + padre +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", vigencia=" + vigencia +
                ", caducidad=" + caducidad +
                ", estado='" + estado + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}