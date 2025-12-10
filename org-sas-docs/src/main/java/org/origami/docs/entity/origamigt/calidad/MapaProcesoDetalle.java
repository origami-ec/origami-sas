package org.origami.docs.entity.origamigt.calidad;

public class MapaProcesoDetalle {
    private Long id;
    private String proceso;
    private String estado;
    private String mapaProcesoDescripcion;
    private MapaProceso mapaProceso;

    public MapaProcesoDetalle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMapaProcesoDescripcion() {
        return mapaProcesoDescripcion;
    }

    public void setMapaProcesoDescripcion(String mapaProcesoDescripcion) {
        this.mapaProcesoDescripcion = mapaProcesoDescripcion;
    }

    public MapaProceso getMapaProceso() {
        return mapaProceso;
    }

    public void setMapaProceso(MapaProceso mapaProceso) {
        this.mapaProceso = mapaProceso;
    }
}
