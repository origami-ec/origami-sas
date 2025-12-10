package org.ibarra.util.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Estado type.
 */
public enum EstadoTramite {

    FINALIZADO("Finalizado", "finalizado", "blue"),
    ANULADO("Anulado", "anulado", "rgb(224,154,134)"),
    EN_EJECUCIÓN("En Ejecución", "ejecucion", "white"),
    SUSPENDIDO("Suspendido", "suspendido", "red");

    @JsonValue
    String name = this.name();
    String descripcion;
    String cssClass;
    String textColor;

    EstadoTramite(String descripcion, String cssClass, String textColor) {
        this.descripcion = descripcion;
        this.cssClass = cssClass;
        this.textColor = textColor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EstadoTramite{" +
                "descripcion='" + descripcion + '\'' +
                ", cssClass='" + cssClass + '\'' +
                ", textColor='" + textColor + '\'' +
                '}';
    }
}
