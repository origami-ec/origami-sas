package org.origami.docs.entity.origamigt.configuracion;


public class FormatoCodigoCuenta {
    private Long id;
    private String caracter;
    private String codigo;
    private String estado;
    private Integer nivel;
    private Integer numDigito;
    private Boolean programatico;
    private Boolean separador;
    private Long tipoEstructura;

    public FormatoCodigoCuenta() {

    }

    public FormatoCodigoCuenta(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
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

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getNumDigito() {
        return numDigito;
    }

    public void setNumDigito(Integer numDigito) {
        this.numDigito = numDigito;
    }

    public Boolean getProgramatico() {
        return programatico;
    }

    public void setProgramatico(Boolean programatico) {
        this.programatico = programatico;
    }

    public Boolean getSeparador() {
        return separador;
    }

    public void setSeparador(Boolean separador) {
        this.separador = separador;
    }

    public Long getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(Long tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }
}
