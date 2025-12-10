package org.ibarra.util.model;

import org.activiti.bpmn.model.GraphicInfo;
import org.apache.batik.ext.awt.geom.Polyline2D;
import org.ibarra.util.Utils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public class Grafico {
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Direccion direccion;
    private Punto puntoLLegada;


    private String keyIntersecta;
    private List<GraphicInfo> intersecciones;
    private Polyline2D poligono;

    public Grafico() {
    }

    public Grafico(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Grafico(double x, double y, double width, double height) {
        this.height = height;
        this.width = width;
        this.y = y;
        this.x = x;
    }

    public Double getX() {
        x = Utils.round(x, 1);
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        y = Utils.round(y, 1);
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getWidth() {
        width = Utils.round(width, 1);
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        height = Utils.round(height, 1);
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getKeyIntersecta() {
        return keyIntersecta;
    }

    public void setKeyIntersecta(String keyIntersecta) {
        this.keyIntersecta = keyIntersecta;
    }

    public List<GraphicInfo> getIntersecciones() {
        return intersecciones;
    }

    public void setIntersecciones(List<GraphicInfo> intersecciones) {
        this.intersecciones = intersecciones;
    }

    public Polyline2D getPoligono() {
        return poligono;
    }

    public void setPoligono(Polyline2D poligono) {
        this.poligono = poligono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Punto getPuntoLLegada() {
        return puntoLLegada;
    }

    public void setPuntoLLegada(Punto puntoLLegada) {
        this.puntoLLegada = puntoLLegada;
    }

    public Point2D getIntersection() {

        final double x1, y1, x2, y2, x3, y3, x4, y4;
        Rectangle rectangle = poligono.getBounds();
        Line2D.Double line1 = new Line2D.Double(rectangle.getX(), rectangle.getY(), rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        Line2D.Double line2 = new Line2D.Double(this.x, this.y, this.x + this.width, this.y + this.height);
        x1 = line1.x1;
        y1 = line1.y1;
        x2 = line1.x2;
        y2 = line1.y2;
        x3 = line2.x1;
        y3 = line2.y1;
        x4 = line2.x2;
        y4 = line2.y2;
        final double x = (
                (x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3) * (x1 * y2 - x2 * y1)) /
                ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        final double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2) * (x3 * y4 - x4 * y3)) /
                ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        return new Point2D.Double(Utils.round(x, 1), Utils.round(y, 1));

    }


    @Override
    public String toString() {
        return "Grafico{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public enum Direccion {
        RECTO_HACIA_ABAJO(0),
        RECTO_HACIA_ARRIBA(1),
        HACIA_ABAJO_DERECHA(2),
        HACIA_ABAJO_IZQUIERDA(3),
        HACIA_ARRIBA_DERECHA(4),
        HACIA_ARRIBA_IZQUIERDA(5),
        DERECHA_IZQUIERDAD(6),
        IZQUIERDA_DERECHA(7);

        int direccion;

        Direccion(int direccion) {
            this.direccion = direccion;
        }
    }

    public static class Punto {
        Double x;
        Double y;

        public Punto() {
        }

        public Punto(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            x = Utils.round(x, 1);
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            y = Utils.round(y, 1);
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }
}
