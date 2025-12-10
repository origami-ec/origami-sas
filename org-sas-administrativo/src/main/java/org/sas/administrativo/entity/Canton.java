package org.sas.administrativo.entity;


import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "canton", schema = EsquemaConfig.configuracion)
public class Canton implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "provincia", referencedColumnName = "id")
    private Provincia provincia;
    @Column(name = "cod_provincia")
    private String codProvincia;
    @Column(name = "cod_canton")
    private String codCanton;
    private String codigo;
    private String canton;
    private String descripcion;
    private String estado;

    public Canton() {

    }

    public Canton(Long id) {
        this.id = id;
    }

    public Canton(String codigo) {
        this.codigo = codigo;
    }

    public Canton(Long id, Provincia provincia) {
        this.id = id;
        this.provincia = provincia;
    }

    public Canton(String canton, Boolean bool){
        this.canton = canton;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getCodCanton() {
        return codCanton;
    }

    public void setCodCanton(String codCanton) {
        this.codCanton = codCanton;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        Canton canton = (Canton) o;
        return id.equals(canton.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Canton{" +
                "id=" + id +
                ", provincia=" + provincia +
                ", codProvincia='" + codProvincia + '\'' +
                ", codCanton='" + codCanton + '\'' +
                ", codigo='" + codigo + '\'' +
                ", canton='" + canton + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
