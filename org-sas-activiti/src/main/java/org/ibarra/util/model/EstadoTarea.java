package org.ibarra.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Estado type.
 */
public enum EstadoTarea {

    ASSIGNED_ON_TIME("Asignada a Tiempo", "lightgreen", "#707173"),
    UNASSIGNED("SIN ASIGNAR", "rgb(248,214,214)", "#707173"),
    NEARING_DEADLINE("Por vencer", "yellow", "#707173"),
    OUT_OF_TIME("Fuera de tiempo", "red", "white"),
    COMPLETED_ON_TIME("Completada a Tiempo", "blue", "white"),
    COMPLETED_LATE("Completada fuera de Tiempo", "darkorange", "#707173");

    @JsonValue
    String name = this.name();
    String descripcion;
    String color;
    String textColor;

    EstadoTarea(String descripcion, String color, String textColor) {
        this.descripcion = descripcion;
        this.color = color;
        this.textColor = textColor;
    }

    public String getName() {
        return name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getColor() {
        return color;
    }

    public String getTextColor() {
        return textColor;
    }


}
