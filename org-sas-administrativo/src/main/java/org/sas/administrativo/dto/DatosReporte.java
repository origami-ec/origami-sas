package org.sas.administrativo.dto;


import org.sas.administrativo.dto.documental.ArchivoIndexDto;
import org.sas.administrativo.dto.documental.UsuarioDocs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatosReporte {

    private String nombreReporte;
    private String nombreArchivo;
    private String formato;
    private UsuarioDocs usuarioDocs;
    private Boolean dataSource; //True si es por un modelo de datos : False si es x consulta SQL
    private Boolean gestorDocumental; //True si se necesita guardar el reporte en el gestor documental
    private ArchivoIndexDto archivoIndex;
    private Map<String, Object> parametros;
    private List dataList;
    private List<DatosReporte> reporteAgregados;

    private String urlWebService;
    //
    private List<DatosReporte> reporteAgregadosAd;
    private Boolean onePagePerSheet = true;
    private Boolean whitePageBackground = true;
    private Boolean ignoreGraphics = true;
    private Boolean ignoreTextFormatting = true;
    private Boolean autoFitRow = false;
    private Boolean fontSizeFixEnabled = false;
    private Boolean ignoreCellBackground = false;
    private Boolean showGridLines = true;
    private Boolean wrapText = false;
    private Integer fitHeight = 60;
    private Integer fitWidth = 160;
    private Boolean unirReportAdc = false;

    public DatosReporte() {
    }


    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Boolean getDataSource() {
        return dataSource;
    }

    public void setDataSource(Boolean dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getGestorDocumental() {
        return gestorDocumental;
    }

    public void setGestorDocumental(Boolean gestorDocumental) {
        this.gestorDocumental = gestorDocumental;
    }

    public ArchivoIndexDto getArchivoIndex() {
        return archivoIndex;
    }

    public void setArchivoIndex(ArchivoIndexDto archivoIndex) {
        this.archivoIndex = archivoIndex;
    }

    public UsuarioDocs getUsuarioDocs() {
        return usuarioDocs;
    }

    public void setUsuarioDocs(UsuarioDocs usuarioDocs) {
        this.usuarioDocs = usuarioDocs;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public List<DatosReporte> getReporteAgregados() {
        return reporteAgregados;
    }

    public void setReporteAgregados(List<DatosReporte> reporteAgregados) {
        this.reporteAgregados = reporteAgregados;
    }

    public List<DatosReporte> getReporteAgregadosAd() {
        return reporteAgregadosAd;
    }

    public void setReporteAgregadosAd(List<DatosReporte> reporteAgregadosAd) {
        this.reporteAgregadosAd = reporteAgregadosAd;
    }

    public Boolean getOnePagePerSheet() {
        return onePagePerSheet;
    }

    public void setOnePagePerSheet(Boolean onePagePerSheet) {
        this.onePagePerSheet = onePagePerSheet;
    }

    public Boolean getWhitePageBackground() {
        return whitePageBackground;
    }

    public void setWhitePageBackground(Boolean whitePageBackground) {
        this.whitePageBackground = whitePageBackground;
    }

    public Boolean getIgnoreGraphics() {
        return ignoreGraphics;
    }

    public void setIgnoreGraphics(Boolean ignoreGraphics) {
        this.ignoreGraphics = ignoreGraphics;
    }

    public Boolean getIgnoreTextFormatting() {
        return ignoreTextFormatting;
    }

    public void setIgnoreTextFormatting(Boolean ignoreTextFormatting) {
        this.ignoreTextFormatting = ignoreTextFormatting;
    }

    public Boolean getAutoFitRow() {
        return autoFitRow;
    }

    public void setAutoFitRow(Boolean autoFitRow) {
        this.autoFitRow = autoFitRow;
    }

    public Boolean getFontSizeFixEnabled() {
        return fontSizeFixEnabled;
    }

    public void setFontSizeFixEnabled(Boolean fontSizeFixEnabled) {
        this.fontSizeFixEnabled = fontSizeFixEnabled;
    }

    public Boolean getIgnoreCellBackground() {
        return ignoreCellBackground;
    }

    public void setIgnoreCellBackground(Boolean ignoreCellBackground) {
        this.ignoreCellBackground = ignoreCellBackground;
    }

    public Boolean getShowGridLines() {
        return showGridLines;
    }

    public void setShowGridLines(Boolean showGridLines) {
        this.showGridLines = showGridLines;
    }

    public Boolean getWrapText() {
        return wrapText;
    }

    public void setWrapText(Boolean wrapText) {
        this.wrapText = wrapText;
    }

    public Integer getFitHeight() {
        return fitHeight;
    }

    public void setFitHeight(Integer fitHeight) {
        this.fitHeight = fitHeight;
    }

    public Integer getFitWidth() {
        return fitWidth;
    }

    public void setFitWidth(Integer fitWidth) {
        this.fitWidth = fitWidth;
    }

    public Boolean getUnirReportAdc() {
        return unirReportAdc;
    }

    public void setUnirReportAdc(Boolean unirReportAdc) {
        this.unirReportAdc = unirReportAdc;
    }

    public void addReporteAd(DatosReporte datRepAd) {
        if (reporteAgregadosAd == null) {
            reporteAgregadosAd = new LinkedList<>();
        }
        reporteAgregadosAd.add(datRepAd);
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    public void setUrlWebService(String urlWebService) {
        this.urlWebService = urlWebService;
    }

    @Override
    public String toString() {
        return "DatosReporte{" +
                "nombreReporte:'" + nombreReporte + '\'' +
                ", nombreArchivo:'" + nombreArchivo + '\'' +
                ", formato:'" + formato + '\'' +
                ", usuarioDocs:" + usuarioDocs +
                ", dataSource:" + dataSource +
                ", gestorDocumental:" + gestorDocumental +
                ", archivoIndex:" + archivoIndex +
                ", parametros:" + parametros +
                '}';
    }
}
