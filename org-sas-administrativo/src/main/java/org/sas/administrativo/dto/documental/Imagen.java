package org.sas.administrativo.dto.documental;

public class Imagen {
    private Integer indice; //es el indice del arreglo basicamente es el numero de la pagina cuando empieza en 0
    private String descripcion;
    private String apiUrl;
    private String ruta;
//    private List<Nota> notas;

    public Imagen() {
    }

    public Imagen(String descripcion, String apiUrl, String ruta) {
        this.descripcion = descripcion;
        this.apiUrl = apiUrl;
        this.ruta = ruta;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

//    public List<Nota> getNotas() {
//        return notas;
//    }
//
//    public void setNotas(List<Nota> notas) {
//        this.notas = notas;
//    }

    @Override
    public String toString() {
        return "Imagen{" +
                "indice=" + indice +
                ", descripcion='" + descripcion + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", ruta='" + ruta + '\'' +
//                ", notas=" + notas +
                '}';
    }
}
