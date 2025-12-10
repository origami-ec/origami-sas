package org.sas.administrativo.service.configuracion;

import com.google.gson.Gson;
import org.sas.administrativo.conf.AppProps;
import org.sas.administrativo.dto.DatosReporte;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.documental.ArchivoIndexDto;
import org.sas.administrativo.dto.documental.UsuarioDocs;
import org.sas.administrativo.repository.configuracion.ReporteRepo;
import org.sas.administrativo.service.documental.DocumentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ReporteService {

//    @Value("${spring.datasource.driver-class-name}")
//    private String driver;
//    @Value("${spring.datasource.url}")
//    private String jdbc;
//    @Value("${spring.datasource.username}")
//    private String usuario;
//    @Value("${spring.datasource.password}")
//    private String clave;


    @Autowired
    private AppProps appProps;
    @Autowired
    private ReporteRepo repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentalService documentalService;


    public String generarReporteDocumentalPDF(byte[] reporte, String nombreArchivo, ArchivoIndexDto ai) {
        try {
            if (reporte != null) {
                if (ai != null) {
                    String archivo = documentalService.guardarGestorDocumental(ai.getReferenciaId(), ai.getReferencia(),
                            ai.getDetalleDocumento(), nombreArchivo, ai.getTipoIndexacion(), new UsuarioDocs(ai.getUsuarioDoc()), reporte, null);
                    return archivo;
                }
                return "";
            }
            return "";
        } catch (Exception e) {
            Logger.getLogger(ReporteService.class.getName()).log(Level.SEVERE, null, e);
            return "";
        }
    }

}
