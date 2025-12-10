package org.sas.administrativo.entity.configuracion;


import org.sas.administrativo.util.EsquemaConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "secuencia_general", schema = EsquemaConfig.configuracion)
public class SecuenciaGeneral implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long secuencia;
    private Integer anio;
    private Long tipo;

    public SecuenciaGeneral() {
    }

    public SecuenciaGeneral(Integer anio) {
        this.anio = anio;
    }

    public SecuenciaGeneral(String code, Long secuencia) {
        this.code = code;
        this.secuencia = secuencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Long secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecuenciaGeneral that = (SecuenciaGeneral) o;
        return Objects.equals(id, that.id);
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SecuenciaGeneral{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", secuencia=" + secuencia +
                ", anio=" + anio +
                ", tipo=" + tipo +
                '}';
    }
}
