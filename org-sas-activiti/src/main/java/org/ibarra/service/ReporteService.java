package org.ibarra.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.ibarra.conf.AppProps;
import org.ibarra.dto.DatosReporte;
import org.ibarra.util.Utils;
import org.ibarra.util.model.ReporteFormato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ReporteService {
    private final static Logger LOG = Logger.getLogger(ReporteService.class.getName());

    @Autowired
    private AppProps appProps;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String jdbc;
    @Value("${spring.datasource.username}")
    private String usuario;
    @Value("${spring.datasource.password}")
    private String clave;

    public byte[] generarReporte(DatosReporte datosReporte) {
        try {
            Map<String, Object> par = datosReporte.getParametros();
            if (par == null) {
                par = new HashMap<>();
            }
            if (appProps.getImagenes() != null) {
                par.putAll(Utils.getUrlsImagenesSolicitud(appProps.getImagenes()));
            }
            if (!par.containsKey("SUBREPORT_DIR")) {
                par.put("SUBREPORT_DIR", appProps.getReportes());
            }
            byte[] bytes;
            if (!datosReporte.getDataSource()) {
                bytes = generarJasperXConexion(Utils.armarRutaJasper(appProps.getReportes(),
                        datosReporte.getNombreReporte()), par, datosReporte);
            } else {
                bytes = generarJasperXModelo(Utils.armarRutaJasper(appProps.getReportes(),
                        datosReporte.getNombreReporte()), par, datosReporte);
            }
            return bytes;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public byte[] generarReportexUrlImagenes(DatosReporte datosReporte, Map urlImagenes) {
        try {
            Map<String, Object> par = datosReporte.getParametros();
            if (par == null) {
                par = new HashMap<>();
            }
            if (appProps.getImagenes() != null) {
                par.putAll(urlImagenes);
            }
            par.put("SUBREPORT_DIR", appProps.getReportes());
            byte[] bytes;
            if (!datosReporte.getDataSource()) {
                bytes = generarJasperXConexion(Utils.armarRutaJasper(appProps.getReportes(),
                        datosReporte.getNombreReporte()), par, datosReporte);
            } else {
                bytes = generarJasperXModelo(Utils.armarRutaJasper(appProps.getReportes(),
                        datosReporte.getNombreReporte()), par, datosReporte);
            }
            return bytes;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    private byte[] generarJasperXConexion(String resourceLocation, Map<String, Object> parameters, DatosReporte datosReporte) {
        try {
            Connection con = getConnection();
            File file = ResourceUtils.getFile(resourceLocation);
            System.out.println("resourceLocation " + file.getAbsolutePath() + " Existe reporte " + file.exists() + " parameters " + parameters);
            JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, con);
            byte[] bytes = bytesReporte(datosReporte, jasperPrint);
            con.close();
            return bytes;
        } catch (FileNotFoundException | JRException | SQLException e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    private byte[] generarJasperXModelo(String resourceLocation, Map<String, Object> parameters,
                                        DatosReporte datosReporte) {
        try {
            System.out.println("Model " + datosReporte.getDataList());
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(datosReporte.getDataList());
            JasperPrint jasperPrint;
            File file = ResourceUtils.getFile(resourceLocation);
            jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters,
                    jrBeanCollectionDataSource);
            return bytesReporte(datosReporte, jasperPrint);
        } catch (FileNotFoundException | JRException e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    private byte[] bytesReporte(DatosReporte datosReporte, JasperPrint jasperPrint) {
        byte[] arr = null;
        try {
            switch (getByCodigo(datosReporte.getFormato())) {
                case PDF:
                    arr = JasperExportManager.exportReportToPdf(jasperPrint);
                    break;
                case EXCEL:
                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    Map<String, Object> par = datosReporte.getParametros();
                    if (par == null) {
                        par = new HashMap<>();
                    }
                    configuration.setOnePagePerSheet((Boolean) par.get("IS_ONE_PAGE_PER_SHEET"));
                    configuration.setIgnoreGraphics(false);
                    String f = appProps.getReportes() + datosReporte.getNombreArchivo();
                    File outputFile = File.createTempFile(f, "xlsx");
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); OutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                        Exporter exporter = new JRXlsxExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
                        exporter.setConfiguration(configuration);
                        exporter.exportReport();
                        byteArrayOutputStream.writeTo(fileOutputStream);
                    }
                    if (outputFile.exists()) {
                        arr = Files.readAllBytes(outputFile.toPath());
                        outputFile.delete();
                    }
                    break;
                case CSV:
                    String f1 = appProps.getReportes() + datosReporte.getNombreArchivo();
                    File outputFile1 = File.createTempFile(f1, "csv");
                    JRCsvExporter csvExporter = new JRCsvExporter();
                    csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    csvExporter.setExporterOutput(new SimpleWriterExporterOutput(new FileOutputStream(outputFile1)));
                    csvExporter.exportReport();
                    break;
                case HTML:
                    String f2 = appProps.getReportes() + datosReporte.getNombreArchivo();
                    File outputFile2 = File.createTempFile(f2, "csv");
                    HtmlExporter htmlExporter = new HtmlExporter();
                    htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(outputFile2));
                    htmlExporter.exportReport();
                    break;
                case WORD:
                    break;
            }
        } catch (IOException | JRException e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return arr;
    }

    public ReporteFormato getByCodigo(String codigo) {
        for (ReporteFormato value : ReporteFormato.values()) {
            if (value.getCodigo().equals(codigo)) {
                return value;
            }
        }
        return ReporteFormato.PDF;
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReporteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties props = new Properties();
        props.setProperty("user", usuario);
        props.setProperty("password", clave);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbc, props);
        } catch (SQLException ex) {
            Logger.getLogger(ReporteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    /**
     * @param parametrosReporte los parametros que recibe el reporte
     * @param nombreReporte     el nombre del archivo, estos se crean en
     *                          ValoresCodigo.java
     * @param nombreArchivo     SOLO el nombre del archivo, NO debe ser ruta NI
     *                          extension NI nada mas que solo sea el NOMBRE
     * @return
     */
    public byte[] generarReportePDF(Map<String, Object> parametrosReporte, String nombreReporte, String nombreArchivo) {
        DatosReporte datosReporte = new DatosReporte();
        datosReporte.setDataSource(Boolean.FALSE);
        datosReporte.setFormato(ReporteFormato.PDF.getCodigo());
        datosReporte.setNombreReporte(nombreReporte);
        datosReporte.setNombreArchivo(nombreArchivo + ReporteFormato.PDF.getExtension());
        datosReporte.setParametros(parametrosReporte);
        byte[] reporte = generarReporte(datosReporte);
        return reporte;
    }

    private byte[] bytesReporte(DatosReporte datosReporte, JasperPrint jasperPrint, OutputStream outputStream, List<byte[]> repAdd) {
        byte[] arr = null;
        try {
            switch (getByCodigo(datosReporte.getFormato())) {
                case PDF:
                    if (outputStream == null) {
                        arr = JasperExportManager.exportReportToPdf(jasperPrint);
                    } else {
                        if (repAdd != null) {
                            byte[] pdfPrin = JasperExportManager.exportReportToPdf(jasperPrint);
                            List<byte[]> pdfs = new LinkedList<>();
                            pdfs.add(pdfPrin);
                            pdfs.addAll(repAdd);
                        } else {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                        }
                    }
                    break;
                case EXCEL:
                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(datosReporte.getOnePagePerSheet());
                    configuration.setWhitePageBackground(datosReporte.getWhitePageBackground());
                    configuration.setIgnoreTextFormatting(datosReporte.getIgnoreTextFormatting());
                    configuration.setAutoFitRow(datosReporte.getAutoFitRow());
                    configuration.setIgnoreGraphics(datosReporte.getIgnoreGraphics());
                    configuration.setFontSizeFixEnabled(datosReporte.getFontSizeFixEnabled());

                    configuration.setIgnoreCellBackground(datosReporte.getIgnoreCellBackground());
                    configuration.setShowGridLines(datosReporte.getShowGridLines());
                    configuration.setWrapText(datosReporte.getWrapText());
                    configuration.setFitHeight(datosReporte.getFitHeight());
                    configuration.setFitWidth(datosReporte.getFitHeight());
                    configuration.setDetectCellType(true);
                    configuration.setFontSizeFixEnabled(Boolean.FALSE);
                    configuration.setCollapseRowSpan(false);
                    configuration.setMaxRowsPerSheet(1048000);
                    configuration.setRemoveEmptySpaceBetweenColumns(true);
                    //configuration.setRemoveEmptySpaceBetweenRows(true);
                    configuration.setRemoveEmptySpaceBetweenRows((Boolean) ((datosReporte.getParametros().get("REMOVE_EMPTY_SPACE_BETWEEN_ROWS")) == null ? Boolean.TRUE : datosReporte.getParametros().get("REMOVE_EMPTY_SPACE_BETWEEN_ROWS")));
                    configuration.setAutoFitPageHeight(false);
                    configuration.setIgnoreAnchors(true);
                    configuration.setIgnorePageMargins(true);
//                    ExporterFilter filter = new JRXlsxExporterNature();
//                    filter.
//                    configuration.setExporterFilter(filter);
                    //configuration.setIgnoreCellBorder(true);
                    //configuration.setIgnoreTextFormatting(true);
                    //configuration.setShrinkToFit(true);
                    if (outputStream == null) {
                        LOG.info("bytesReporte excel byte");
                        File outputFile = File.createTempFile(datosReporte.getNombreArchivo(), "xlsx");
                        try (OutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                            Exporter<ExporterInput, XlsxReportConfiguration, XlsxExporterConfiguration, OutputStreamExporterOutput> exporter = new JRXlsxExporter();
                            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileOutputStream));
                            exporter.setConfiguration(configuration);
                            exporter.exportReport();
                            if (outputFile.exists()) {
                                arr = Files.readAllBytes(outputFile.toPath());
                                outputFile.delete();
                            }
                        }
//                    if (outputFile.exists()) {
//                        arr = Files.readAllBytes(outputFile.toPath());
//                        outputFile.delete();
//                    }
                    } else {
//                        LOG.info("bytesReporte excel outputStream");
                        Exporter<ExporterInput, XlsxReportConfiguration, XlsxExporterConfiguration, OutputStreamExporterOutput> exporter = new JRXlsxExporter();
                        SimpleReportContext cx = new SimpleReportContext();
                        cx.setParameterValue("net.sf.jasperreports.xpath.executer.factory", "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
                        cx.setParameterValue("net.sf.jasperreports.export.xls.exclude.origin.band.1", "pageHeader");
                        exporter.setReportContext(cx);
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                        exporter.setConfiguration(configuration);
                        exporter.exportReport();
                    }
                    break;
                case WORD:
                    if (outputStream != null) {
                        JRDocxExporter exporter = new JRDocxExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                        exporter.exportReport();
                    } else {
                        Exporter exporter = new JRDocxExporter();
                        final ByteArrayOutputStream out = new ByteArrayOutputStream();
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.exportReport();
                        arr = out.toByteArray();
                    }
                    System.out.println("//EL REPORTE ES WORD");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        //LOG.info("Finalizacion de exportacion");
        return arr;
    }

}
