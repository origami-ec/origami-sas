package org.ibarra.dto.flujoDinamico;


import java.util.Objects;

public class CargoDto {
    private Long id;
    private String nombreCargo;
    private String estado;
    private String codigo;
    private Boolean activo;
    private Boolean asignado;
    private UnidadAdministrativaDto unidad;

    public CargoDto() {
    }

    public CargoDto(Long id, String nombreCargo) {
        this.id = id;
        this.nombreCargo = nombreCargo;
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

    public UnidadAdministrativaDto getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativaDto unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        return "CargoDto{" +
                "id=" + id +
                ", nombreCargo='" + nombreCargo + '\'' +
                ", estado='" + estado + '\'' +
                ", codigo='" + codigo + '\'' +
                ", activo=" + activo +
                ", asignado=" + asignado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoDto cargoDto = (CargoDto) o;
        return Objects.equals(id, cargoDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
