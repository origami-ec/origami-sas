package org.origami.docs.entity.origamigt.calidad;

import java.util.Objects;

public class MapaProcesoNivel {

    private Long id;
    private String descripcion;
    private String nivel;
    private Integer codNivel;
    private String estado;

    public MapaProcesoNivel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getCodNivel() {
        return codNivel;
    }

    public void setCodNivel(Integer codNivel) {
        this.codNivel = codNivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapaProcesoNivel that = (MapaProcesoNivel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapaProcesoNivel{"
                + "id=" + id
                + ", descripcion='" + descripcion + '\''
                + ", nivel='" + nivel + '\''
                + ", codNivel=" + codNivel
                + '}';
    }

}
