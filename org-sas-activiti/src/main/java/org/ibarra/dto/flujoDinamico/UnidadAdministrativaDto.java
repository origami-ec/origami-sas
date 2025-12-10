package org.ibarra.dto.flujoDinamico;

import org.ibarra.dto.CatalogoItemDto;
import org.ibarra.dto.ServidorDatos;

public class UnidadAdministrativaDto {
    private Long id;
    private UnidadAdministrativaDto padre;
    private String codigo;
    private String estado;
    private String ubicacion;
    private String nombre;
    private CatalogoItemDto tipo;
    private ServidorDatos responsable;
    private ServidorDatos director;

    public UnidadAdministrativaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadAdministrativaDto getPadre() {
        return padre;
    }

    public void setPadre(UnidadAdministrativaDto padre) {
        this.padre = padre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CatalogoItemDto getTipo() {
        return tipo;
    }

    public void setTipo(CatalogoItemDto tipo) {
        this.tipo = tipo;
    }

    public ServidorDatos getResponsable() {
        return responsable;
    }

    public void setResponsable(ServidorDatos responsable) {
        this.responsable = responsable;
    }

    public ServidorDatos getDirector() {
        return director;
    }

    public void setDirector(ServidorDatos director) {
        this.director = director;
    }
}
