package org.ibarra.util.model;

/**
 * The type Correo archivo.
 */
public class CorreoArchivo {
    private String nombreArchivo;
    private String tipoArchivo;
    private String archivoBase64;

    /**
     * Instantiates a new Correo archivo.
     */
    public CorreoArchivo() {
    }

    /**
     * Instantiates a new Correo archivo.
     *
     * @param nombreArchivo the nombre archivo
     */
    public CorreoArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * Gets nombre archivo.
     *
     * @return the nombre archivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Sets nombre archivo.
     *
     * @param nombreArchivo the nombre archivo
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * Gets tipo archivo.
     *
     * @return the tipo archivo
     */
    public String getTipoArchivo() {
        return tipoArchivo;
    }

    /**
     * Sets tipo archivo.
     *
     * @param tipoArchivo the tipo archivo
     */
    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    /**
     * Gets archivo base 64.
     *
     * @return the archivo base 64
     */
    public String getArchivoBase64() {
        return archivoBase64;
    }

    /**
     * Sets archivo base 64.
     *
     * @param archivoBase64 the archivo base 64
     */
    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }
}
