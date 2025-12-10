package org.origami.docs.entity.origamigt.configuracion;

public class UnidadAdministrativa {

    private Long id;
    private UnidadAdministrativa padre;
    private String codigo;
    private String estado;
    private String ubicacion;
    private String nombre;

    public UnidadAdministrativa() {
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
}
