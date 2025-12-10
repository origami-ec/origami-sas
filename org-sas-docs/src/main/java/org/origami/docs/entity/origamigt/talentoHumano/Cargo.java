package org.origami.docs.entity.origamigt.talentoHumano;

import org.origami.docs.entity.origamigt.configuracion.CatalogoItem;

import java.util.Date;
import java.util.List;

public class Cargo {
    private Long id;
    private String nombreCargo;
    private String estado;
    private Date fechaActualizacion;
    private UnidadAdministrativa unidad;
    private RegimenLaboral regimen;
    private EscalaSalarial escalaSalarial;
    private String codigo;
    private Boolean activo;
    private Boolean asignado;
    private CatalogoItem catalogoItem;
    private Long contrato;
    private Long tempo;
    private Integer numRegistro;
    private Integer periodo;
    private List<CargoRubro> cargoRubros;

    public Cargo() {
    }

    public List<CargoRubro> getCargoRubros() {
        return cargoRubros;
    }

    public void setCargoRubros(List<CargoRubro> cargoRubros) {
        this.cargoRubros = cargoRubros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public Long getContrato() {
        return contrato;
    }

    public void setContrato(Long contrato) {
        this.contrato = contrato;
    }

    public Long getTempo() {
        return tempo;
    }

    public void setTempo(Long tempo) {
        this.tempo = tempo;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public RegimenLaboral getRegimen() {
        return regimen;
    }

    public void setRegimen(RegimenLaboral regimen) {
        this.regimen = regimen;
    }

    public EscalaSalarial getEscalaSalarial() {
        return escalaSalarial;
    }

    public void setEscalaSalarial(EscalaSalarial escalaSalarial) {
        this.escalaSalarial = escalaSalarial;
    }

    public CatalogoItem getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(CatalogoItem catalogoItem) {
        this.catalogoItem = catalogoItem;
    }
}
