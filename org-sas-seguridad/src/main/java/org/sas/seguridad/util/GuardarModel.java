package org.sas.seguridad.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GuardarModel implements Serializable {

    private String entity;
    private String data;
    private Map<String, Validacion> validaciones;


    public GuardarModel() {
    }

    public GuardarModel(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, Validacion> getValidaciones() {
        return validaciones;
    }

    public void setValidaciones(Map<String, Validacion> validaciones) {
        this.validaciones = validaciones;
    }

    public void addValidation(String campo, String descripcionCampo) {
        if (validaciones == null) {
            validaciones = new LinkedHashMap<>();
        }
        Validacion v = new Validacion(descripcionCampo);
        validaciones.put(campo, v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuardarModel)) return false;
        GuardarModel that = (GuardarModel) o;
        return entity.equals(that.entity) && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, data);
    }

    @Override
    public String toString() {
        return "GuardarModel{" +
                "entity='" + entity + '\'' +
                ", data='" + data + '\'' +
                ", validaciones=" + validaciones +
                '}';
    }

    public static class Validacion {

        private String descripcionCampo;
        private List<String> acciones;

        public Validacion(String descripcionCampo) {
            this.descripcionCampo = descripcionCampo;
        }

        public String getDescripcionCampo() {
            return descripcionCampo;
        }

        public void setDescripcionCampo(String descripcionCampo) {
            this.descripcionCampo = descripcionCampo;
        }

        public List<String> getAcciones() {
            return acciones;
        }

        public void setAcciones(List<String> acciones) {
            this.acciones = acciones;
        }

        @Override
        public String toString() {
            return "Validacion{" +
                    "descripcionCampo='" + descripcionCampo + '\'' +
                    ", acciones=" + acciones +
                    '}';
        }
    }
}
