package org.sas.seguridad.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "valor")
public class Valor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String valorString;
    private BigDecimal valorNumeric;

    public Valor() {
    }

    public Valor(String code) {
        this.code = code;
    }

    public Valor(Long id) {
        this.id = id;
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

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public BigDecimal getValorNumeric() {
        return valorNumeric;
    }

    public void setValorNumeric(BigDecimal valorNumeric) {
        this.valorNumeric = valorNumeric;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Valor)) {
            return false;
        }
        Valor other = (Valor) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Valores{" + "id=" + id + ", code=" + code + ", valorString=" + valorString + ", valorNumeric=" + valorNumeric + '}';
    }
}
