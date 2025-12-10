package org.sas.mail.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document("correoFormato")
public class CorreoFormato implements Serializable {

    @BsonId
    private String id;
    private String encabezado;
    private String pieDePagina;

    public CorreoFormato() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getPieDePagina() {
        return pieDePagina;
    }

    public void setPieDePagina(String pieDePagina) {
        this.pieDePagina = pieDePagina;
    }
}
