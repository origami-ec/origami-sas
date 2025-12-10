package org.sas.administrativo.entity;

import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "parroquia", schema = EsquemaConfig.configuracion)
public class Parroquia implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String codigo;

    @Column(name = "lat")
    private Double latitud;

    @Column(name = "lnt")
    private Double longitud;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "canton", referencedColumnName = "id")
    private Canton canton;

    private String estado;

    public Parroquia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parroquia parroquia = (Parroquia) o;
        return id.equals(parroquia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Parroquia{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", nombre='" + nombre + '\'' +
                ", canton=" + canton +
                ", estado='" + estado + '\'' +
                '}';
    }
}
