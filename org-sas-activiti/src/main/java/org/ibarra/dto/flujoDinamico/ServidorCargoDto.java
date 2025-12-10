package org.ibarra.dto.flujoDinamico;

import java.util.Objects;

public class ServidorCargoDto {
    private Long id;
    private Long servidor;
    private CargoDto cargo;
    private String estado;

    public ServidorCargoDto() {
    }

    public ServidorCargoDto(Long id, CargoDto cargo) {
        this.id = id;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServidor() {
        return servidor;
    }

    public void setServidor(Long servidor) {
        this.servidor = servidor;
    }

    public CargoDto getCargo() {
        return cargo;
    }

    public void setCargo(CargoDto cargo) {
        this.cargo = cargo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ServidorCargoDto{" +
                "id=" + id +
                ", servidor=" + servidor +
                ", cargo=" + cargo +
                ", estado='" + estado + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServidorCargoDto that = (ServidorCargoDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
