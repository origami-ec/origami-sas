/*
 * Copyright (C) 2020
 * Authors: Ricardo Arguello, Misael Fernández
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.sas.firmaec.rubrica.sign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.*;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.sas.firmaec.model.FirmaDocDesk;
import org.sas.firmaec.model.FirmaDocumento;
import org.sas.firmaec.model.FirmaDocumentoDB;
import org.sas.firmaec.model.FirmaElectronica;
import org.sas.firmaec.rubrica.certificate.CertEcUtils;
import org.sas.firmaec.rubrica.certificate.CertUtils;
import org.sas.firmaec.rubrica.certificate.to.Documento;
import org.sas.firmaec.rubrica.exceptions.EntidadCertificadoraNoValidaException;
import org.sas.firmaec.rubrica.keystore.Alias;
import org.sas.firmaec.rubrica.keystore.FileKeyStoreProvider;
import org.sas.firmaec.rubrica.keystore.KeyStoreProvider;
import org.sas.firmaec.rubrica.keystore.KeyStoreProviderFactory;
import org.sas.firmaec.rubrica.sign.pdf.PDFSigner;
import org.sas.firmaec.rubrica.sign.pdf.PdfUtil;
import org.sas.firmaec.rubrica.utils.*;
import org.sas.firmaec.rubrica.validaciones.DocumentoUtils;
import org.sas.firmaec.service.SearchSubword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.logging.Logger;

/**
 * Metodo de pruebas funcionales
 *
 * @author mfernandez
 */
@Service
public class FirmaElectronicaService {


    private static final Logger log = Logger.getLogger(FirmaElectronicaService.class.getName());

    @Value("${app.origamiDocs}")
    private String origamiGT;
    @Value("${app.documental}   ")
    private String documental;
    @Value("${app.rutaArchivos}")
    private String rutaArchivos;
    @Value("${app.administrativo}")
    private String administrativo;
    @Value("${app.calidad}")
    private String calidad;
    @Value("${app.ext}")
    private String ext;

    @Value("${app.notificacion}")
    private String notificacion;
    @Value("${app.bddimi}")
    private String bddimi;
    @Value("${app.ventanilla}")
    private String ventanilla;


    public List<Alias> getAllTokens() {
        try {
            String tipoKeyStoreProvider = "TOKEN";
            KeyStore ks = KeyStoreProviderFactory.getKeyStore("", tipoKeyStoreProvider);

            if (ks != null) {
                return CertUtils.getTokens(ks);
            }

        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return new ArrayList<>();
    }


    private static Properties parametros(FirmaElectronica firmaElectronica) {

        try {
            System.out.println("firmaElectronica.toString(): "+firmaElectronica.toString());
            //System.out.println("firmaElectronica.getPosicionX(): " + firmaElectronica.getPosicionX());
            //System.out.println("firmaElectronica.getPosicionY(): " + firmaElectronica.getPosicionY());
            //System.out.println("firmaElectronica.getNumeroPagina(): " + firmaElectronica.getNumeroPagina());
            //System.out.println(firmaElectronica.getPosicionX() == null && firmaElectronica.getPosicionY() == null);
            if (firmaElectronica.getPosicionX() == null && firmaElectronica.getPosicionY() == null) {
                if (firmaElectronica.getPalabraDesde() != null) {
                    System.out.println("firmaElectronica.getPalabraDesde(): " + firmaElectronica.getPalabraDesde());
                    System.out.println("firmaElectronica.getArchivoFirmar(): " + firmaElectronica.getArchivoFirmar());
                    Integer[] fontPosition = getFontPosition(firmaElectronica.getArchivoFirmar(), firmaElectronica.getPalabraDesde());
                    System.out.println("fontPosition>>>" + fontPosition[0] + " " + firmaElectronica.getMovimientoX());
                    System.out.println("fontPosition>>>" + fontPosition[1] + " " + firmaElectronica.getMovimientoY());
                    if (fontPosition != null) {
                        Integer x = fontPosition[0] + (firmaElectronica.getMovimientoX() != null ? firmaElectronica.getMovimientoX() : 0);
                        Integer y = fontPosition[1] + (firmaElectronica.getMovimientoY() != null ? firmaElectronica.getMovimientoY() : 0);
                        firmaElectronica.setPosicionX(x + "");
                        firmaElectronica.setPosicionY(y + "");
                        firmaElectronica.setNumeroPagina(fontPosition[2]);
                    }
                } else if (firmaElectronica.getPalabraHasta() != null) {
                    //System.out.println("firmaElectronica.getPalabraHasta(): " + firmaElectronica.getPalabraHasta());
                    Integer[] fontPosition = getFontPosition(firmaElectronica.getArchivoFirmar(), firmaElectronica.getPalabraHasta());
                    if (fontPosition != null) {
                        Integer x = fontPosition[0] + (firmaElectronica.getMovimientoX() != null ? firmaElectronica.getMovimientoX() : 0);
                        Integer y = fontPosition[1] + (firmaElectronica.getMovimientoY() != null ? firmaElectronica.getMovimientoY() : 0);
                        firmaElectronica.setPosicionX(x + "");
                        firmaElectronica.setPosicionY(y + "");
                        firmaElectronica.setNumeroPagina(fontPosition[2]);
                    }
                } else {
                    System.out.println("Sin texto para busqueda");
                }
            } else {
               /*
                desde el front parec ya venir calculado
                Integer x = Integer.valueOf(firmaElectronica.getPosicionX()) + (firmaElectronica.getMovimientoX() != null ? firmaElectronica.getMovimientoX() : 0);
                Integer y = Integer.valueOf(firmaElectronica.getPosicionY()) + +(firmaElectronica.getMovimientoY() != null ? firmaElectronica.getMovimientoY() : 0);
                firmaElectronica.setPosicionX(x + "");
                firmaElectronica.setPosicionY(y + "");*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Properties params = new Properties();
        params.setProperty(PDFSigner.RUTA_ARCHIVO, firmaElectronica.getArchivoFirmar());
        params.setProperty(PDFSigner.SIGNING_LOCATION, firmaElectronica.getUbicacion());
        params.setProperty(PDFSigner.SIGNING_REASON, firmaElectronica.getMotivo());
        //params.setProperty(PDFSigner.SIGN_TIME, ZonedDateTime.ofInstant(firmaElectronica.getFechaEmision().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        params.setProperty(PDFSigner.SIGN_TIME, ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        params.setProperty(PDFSigner.LAST_PAGE, firmaElectronica.getNumeroPagina().toString());
        params.setProperty(PDFSigner.TYPE_SIG, firmaElectronica.getTipoFirma());
        params.setProperty(PDFSigner.INFO_QR, "Firmado digitalmente con RUBRICA\nhttps://minka.gob.ec/rubrica/rubrica");
        params.setProperty(PDFSigner.FONT_SIZE, "4.5");
        // Posicion firma
        if (firmaElectronica.getTipoFirma().equalsIgnoreCase("QR")) {
            params.setProperty(PDFSigner.FONT_SIZE, "3.0");
        }
        //System.out.println(firmaElectronica.getPosicionX());
        //System.out.println(firmaElectronica.getPosicionY());
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_X, firmaElectronica.getPosicionX());
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, firmaElectronica.getPosicionY());

        return params;
    }

    public FirmaElectronica validarCertificado(FirmaElectronica firmaElectronica) throws IOException, KeyStoreException {

        try {
            System.out.println(rutaArchivos + firmaElectronica.getArchivo());
            System.out.println(rutaArchivos + firmaElectronica.getArchivo() + ext);
            String filename = firmaElectronica.getArchivo().contains("facturacionElectronica") ? firmaElectronica.getArchivo() : rutaArchivos + firmaElectronica.getArchivo() + ext;
            // ARCHIVO
            boolean success;
            File showFile = new File(filename);
            File hideFile = new File(rutaArchivos + firmaElectronica.getArchivo());
            System.out.println("filename: " + filename + " exists: " + showFile.exists());
            if (showFile.exists()) {
                success = true;
            } else {
                success = hideFile.renameTo(showFile);
                System.out.println("hideFile filename: " + showFile.getAbsolutePath() + " exists: " + showFile.exists());
            }
            System.out.println("success " + success);
  //          System.out.println("clave>>firmaElectronica.getClave()>>>" + firmaElectronica.getClave());
            if (success) {
                try {
                    KeyStoreProvider ksp = new FileKeyStoreProvider(filename);

                    KeyStore keyStore = ksp.getKeystore(firmaElectronica.getClave().toCharArray());
                    // TOKEN
                    //KeyStore keyStore = KeyStoreProviderFactory.getKeyStore(PASSWORD);

                    String alias = CertUtils.seleccionarAlias(keyStore);
                    X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
                    firmaElectronica.setUid(Utils.getUID(x509Certificate));
                    firmaElectronica.setCn(Utils.getCN(x509Certificate));
                    firmaElectronica.setEmision(CertEcUtils.getNombreCA(x509Certificate));
                    firmaElectronica.setFechaEmision(x509Certificate.getNotBefore());
                    firmaElectronica.setFechaExpiracion(x509Certificate.getNotAfter());
                    firmaElectronica.setIsuser(x509Certificate.getIssuerX500Principal().getName());

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    String fecha = TiempoUtils.getFechaHoraServidor();
                    System.out.println("\\fecha: " + fecha);
                    TemporalAccessor accessor = dateTimeFormatter.parse(fecha);
                    Date fechaHoraISO = Date.from(Instant.from(accessor));

                    //Validad certificado revocado
                    Date fechaRevocado = UtilsCrlOcsp.validarFechaRevocado(x509Certificate);
                    if (fechaRevocado != null && fechaRevocado.compareTo(fechaHoraISO) <= 0) {
                        firmaElectronica.setEstadoFirma("Certificado revocado: " + fechaRevocado);
                    }
                    if (fechaHoraISO.compareTo(x509Certificate.getNotBefore()) <= 0 || fechaHoraISO.compareTo(x509Certificate.getNotAfter()) >= 0) {
                        firmaElectronica.setEstadoFirma("Certificado caducado");
                    }
                    if (firmaElectronica.getEstadoFirma() == null) {
                        firmaElectronica.setEstadoFirma("Certificado emitido por entidad certificadora acreditada:  " + (Utils.verifySignature(x509Certificate) ? "Si" : "No"));
                    }
                    firmaElectronica.setFirmaCaducada(Boolean.FALSE);
                    if (firmaElectronica.getFechaExpiracion() != null) {
                        Date hoy = new Date();
                        Date expiracion = firmaElectronica.getFechaExpiracion();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(expiracion);
                        calendar.add(Calendar.MONTH, -1); // restar un mes a la fecha

                        Date expiracionProx = calendar.getTime();
                        if (hoy.after(expiracion)) { //LA FIRMA YA SE ENCUENTRA CADUCADA
                            firmaElectronica.setFirmaCaducada(Boolean.TRUE);
                            firmaElectronica.setEstadofirmaCaducada("Su firma electrónica esta vencida por favor renuevela: Fecha de expiración: "
                                    + dateFormatPattern("yyyy-MM-dd", expiracion));
                        } else if (hoy.after(expiracionProx)) {
                            firmaElectronica.setEstadofirmaCaducada("Su firma electrónica esta próxima a caducar: Fecha de expiración: "
                                    + dateFormatPattern("yyyy-MM-dd", expiracion) + " - Faltan: " + diasRestantes(expiracion) + " días.");
                        }
                    }
                } catch (KeyStoreException | EntidadCertificadoraNoValidaException | InvalidKeyException e) {
                    e.printStackTrace();
                    firmaElectronica.setEstadoFirma("Clave incorrecta");
                } catch (Exception e) {
                    e.printStackTrace();
                    firmaElectronica.setEstadoFirma("Clave incorrecta");
                }
                showFile.renameTo(hideFile);

            } else {
                firmaElectronica.setEstadoFirma("No se pudo validar");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmaElectronica;
    }

    public FirmaElectronica firmarDocumentoArchivo(FirmaElectronica firmaElectronica, Boolean esDesk) throws Exception {
        try {

            String firmaRuta;
            // ARCHIVO
            File hideFile = new File(rutaArchivos + firmaElectronica.getArchivo());

            File showFile = null;// SI ES NULO ES POR LA APP DE ESCRITORIO
            Boolean desencripta = Boolean.FALSE;

            if (!esDesk) { //SE AGREGA PARA CONDICIONAR SI ES WEB O NO XK EN LA WEB LAS FIRMAS ESTAN EL SERVIDOR

                showFile = new File(rutaArchivos + firmaElectronica.getArchivo() + ext);
                System.out.println("show file:" + showFile);
                if (showFile.exists()) {
                    esDesk = true;
                } else {
                    esDesk = hideFile.renameTo(showFile);
                }

                firmaRuta = rutaArchivos + firmaElectronica.getArchivo() + ext;
            } else {
                firmaRuta = firmaElectronica.getAliasToken();
            }
            if (esDesk) {
                System.out.println("es desk ");
                // File has been renamed
                KeyStoreProvider ksp = new FileKeyStoreProvider(firmaRuta);
                KeyStore keyStore = ksp.getKeystore(firmaElectronica.getClave().toCharArray());
                if (firmaElectronica.getNumeroPagina() == null) {
                    //PDDocument doc = PDDocument.load(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
                    //firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
                    try (PDDocument doc = PDDocument.load(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())))) {
                        firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
                    }

                }
                byte[] docByteArry = DocumentoUtils.loadFile(replaceRutaArchivo(firmaElectronica.getArchivoFirmar()));
                Properties parametros = parametros(firmaElectronica);

                Signer signer = Utils.documentSigner(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
                String alias = CertUtils.seleccionarAlias(keyStore);

                PrivateKey key = (PrivateKey) keyStore.getKey(alias, firmaElectronica.getClave().toCharArray());
                X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();

                if (x509CertificateUtils.validarX509Certificate((X509Certificate) keyStore.getCertificate(alias))) {//validación de firmaEC
                    try {
                        Certificate[] certChain = keyStore.getCertificateChain(alias);

                        byte[] signed = signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, key, certChain, parametros);
                        System.out.println("final firma\n-------");
                        String nombreDocumento = FileUtils.crearNombreFirmado(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), FileUtils.getExtension(signed));
                        /*java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
                        if (showFile == null) {
                            abrirDocumento(nombreDocumento);
                        }
                        fos.write(signed);
                        fos.close();*/
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento)) {
                            if (showFile == null) {
                                abrirDocumento(nombreDocumento);
                            }
                            fos.write(signed);
                        }

                        File file = new File(nombreDocumento);
                        //firmaElectronica.setArchivoFirmado(file.getName());
                        firmaElectronica.setArchivoFirmado(nombreDocumento);
                        firmaElectronica.setUrlArchivoFirmado(origamiGT + Constantes.apiArchivos + "ar_" + file.getName());
                        guardarDocumentoFirmado(firmaElectronica, nombreDocumento);
                        System.out.println("nombreDocumento: " + nombreDocumento);
                        System.out.println("url archivo firmado:" + firmaElectronica.getUrlArchivoFirmado());
                        if (showFile != null)
                            showFile.renameTo(hideFile);
                    /*if (desencripta) {
                        decryptEncrypt.encrypt(firmaElectronica.getArchivoFirmar());
                        decryptEncrypt.encrypt(nombreDocumento);
                    }*/
                        return firmaElectronica;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FirmaElectronica firmarDocumentoDesk(FirmaDocDesk firmaDocDesk) {
        System.out.println(firmaDocDesk.toString());
        FirmaElectronica firmaElectronica = new FirmaElectronica();
        firmaElectronica.setMotivo(firmaElectronica.getMotivo() != null ? firmaElectronica.getMotivo() : "");
        firmaElectronica.setEsToken(firmaDocDesk.getTipoDesk().equals("TOKEN") ? Boolean.TRUE : Boolean.FALSE);
        firmaElectronica.setClave(firmaDocDesk.getClave());
        firmaElectronica.setNumeroPagina(Integer.valueOf(firmaDocDesk.getPagina()));
        firmaElectronica.setFechaEmision(new Date());
        firmaElectronica.setPosicionX(firmaDocDesk.getPosicionX());
        firmaElectronica.setPosicionY(firmaDocDesk.getPosicionY());
        firmaElectronica.setTipoFirma("QR");
        firmaElectronica.setUbicacion(firmaDocDesk.getUbicacion() != null ? firmaDocDesk.getUbicacion() : "");
        firmaElectronica.setAliasToken(firmaDocDesk.getToken() != null ? firmaDocDesk.getToken() : "");
        firmaElectronica.setArchivoFirmar(firmaDocDesk.getArchivo().trim());
        try {
            if (firmaDocDesk.getTipoDesk().equals("TOKEN")) {
                firmaElectronica = firmarDocumentoToken(firmaElectronica);
            } else {
                firmaElectronica = firmarDocumentoArchivo(firmaElectronica, Boolean.TRUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmaElectronica;
    }


    public FirmaElectronica firmarDocumentoToken(FirmaElectronica firmaElectronica) throws Exception {
        System.out.println("firmarDocumentoToken>>>>>>>>");
        if (firmaElectronica.getNumeroPagina() == null) {
            PDDocument doc = PDDocument.load(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
            firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
        }

        Properties parametros = parametros(firmaElectronica);
        KeyStore ks = KeyStoreProviderFactory.getKeyStore("", "TOKEN");
        String alias = firmaElectronica.getAliasToken();
        X509Certificate x509Certificate = CertUtils.getCert(ks, alias);
        X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();
        if (x509CertificateUtils.validarX509Certificate(x509Certificate) && x509Certificate != null && alias != null) {
            System.out.println("x509CertificateUtils");
            byte[] docSigned = firmar(ks, alias, new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), "".toCharArray(),
                    parametros);
            System.out.println("final firma\n-------");
            String nombreDocumento = FileUtils.crearNombreFirmado(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), FileUtils.getExtension(docSigned));
            java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
            abrirDocumento(nombreDocumento);
            fos.write(docSigned);
            fos.close();
            File file = new File(nombreDocumento);
            //firmaElectronica.setArchivoFirmado(file.getName());
            firmaElectronica.setArchivoFirmado(nombreDocumento);
            firmaElectronica.setUrlArchivoFirmado(origamiGT + Constantes.apiArchivos + "ar_" + file.getName());
            guardarDocumentoFirmado(firmaElectronica, nombreDocumento);
        }


        return firmaElectronica;
    }


    public Documento verificarDocumento(FirmaElectronica firmaElectronica) throws Exception {
        //decryptEncrypt.decrypt(firmaElectronica.getArchivoFirmado());
        System.out.println(firmaElectronica.getArchivoFirmado());
        File document = new File(firmaElectronica.getArchivoFirmado());
        Documento documento = Utils.verificarDocumento(document);
        //decryptEncrypt.encrypt(firmaElectronica.getArchivoFirmado());
        return documento;
    }


    public String replaceRutaArchivo(String ruta) {
        if (ruta.startsWith("ar_")) {
            ruta = ruta.replace("ar_", rutaArchivos);
        }
        return ruta;
    }

    public List<FirmaDocumentoDB> consultarDocumentos(Long servidor, String token) {
        List<FirmaDocumentoDB> documentos = new ArrayList<>();
        try {
            System.out.println("token: " + token);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(mapperDate()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.setBearerAuth(token);
            try {
                log.info("url: " + origamiGT + Constantes.appPlanificacion + Constantes.apiFirmaDocumentos + servidor);
                //ResponseEntity<FirmaDocumentoDB[]> responseEntity = restTemplate.exchange(origamiGT + Constantes.appPlanificacion + Constantes.apiFirmaDocumentos + servidor, HttpMethod.POST, new HttpEntity<>(headers), FirmaDocumentoDB[].class);
                ResponseEntity<FirmaDocumentoDB[]> responseEntity = restTemplate.exchange(origamiGT + Constantes.appPlanificacion + Constantes.apiFirmaDocumentos + servidor, HttpMethod.POST, new HttpEntity<>(headers), FirmaDocumentoDB[].class);
                List<FirmaDocumentoDB> temp = Arrays.asList(responseEntity.getBody());
                if (Utils.isNotEmpty(temp)) {
                    for (FirmaDocumentoDB fd : temp) {
                        fd.setMotivo(Utils.quitarTildes(fd.getMotivo()));
                    }
                    documentos.addAll(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* try {
                log.info("url: " + origamiGT + Constantes.appAdministrativo + Constantes.apiFirmaDocumentos + servidor);
                //ResponseEntity<FirmaDocumentoDB[]> responseEntity = restTemplate.exchange(origamiGT + Constantes.appPlanificacion + Constantes.apiFirmaDocumentos + servidor, HttpMethod.POST, new HttpEntity<>(headers), FirmaDocumentoDB[].class);
                ResponseEntity<FirmaDocumentoDB[]> responseEntity = restTemplate.exchange(origamiGT + Constantes.appAdministrativo + Constantes.apiFirmaDocumentos + servidor, HttpMethod.POST, new HttpEntity<>(headers), FirmaDocumentoDB[].class);
                List<FirmaDocumentoDB> temp = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
                if (Utils.isNotEmpty(temp)) {
                    documentos.addAll(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            if (Utils.isNotEmpty(documentos)) {
                for (FirmaDocumentoDB db : documentos) {
                    db.setMotivo(Utils.quitarTildes(db.getMotivo()));
                    if (Utils.isEmptyString(db.getTramite())) {
                        db.setTramite(" - ");
                    }
                    if (Utils.isNotEmptyString(db.getPalabraDesde())) {
                        db.setPalabraDesde(Utils.stringBase64(db.getPalabraDesde()));
                    }
                    if (Utils.isNotEmptyString(db.getPalabraHasta())) {
                        db.setPalabraHasta(Utils.stringBase64(db.getPalabraHasta()));
                    }


                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return documentos;
    }

    public FirmaDocumentoDB generarDocumentoLocal(FirmaDocumentoDB local) {
        //System.out.println(local.toString());
        if (local.getTipoDesk() != null) {
            if (local.getTipoDesk().equals("ARCHIVO")) {
                if (local.getClave() == null || local.getClave().isEmpty()) {
                    local.setEstadoFirmado("Escriba una contraseña");
                    return local;
                }
                File firma = new File(local.getArchivo());
                if (firma.exists()) {
                } else {
                    local.setEstadoFirmado("No se pudo encontrar su firma electronica");
                    return local;
                }
            }
        }

        if (Utils.isNotEmptyString(local.getPalabraDesde())) {
            local.setPalabraDesde(Utils.base64String(local.getPalabraDesde()));
        }
        if (Utils.isNotEmptyString(local.getPalabraHasta())) {
            local.setPalabraHasta(Utils.base64String(local.getPalabraHasta()));
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String archivo = rutaArchivos + local.getMotivo() + "_" + new Date().getTime() + ".pdf";
        InputStream is = null;


        File target = new File(archivo);
        try {
            System.out.println(local.getDocumento());
            log.info("documental + local.getDocumento(): " + documental.concat(local.getDocumento().trim()));
            ResponseEntity<Resource> responseEntity = restTemplate.exchange(documental + local.getDocumento().trim(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);
            is = responseEntity.getBody().getInputStream();
            try (InputStream inputStream = responseEntity.getBody().getInputStream()) {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, target);
            }
            is.close();
            FirmaElectronica firmaElectronica = new FirmaElectronica();
            firmaElectronica.setArchivoFirmar(archivo);
            firmaElectronica.setClave(local.getClave());
            firmaElectronica.setArchivo(local.getArchivo());
            firmaElectronica.setTipoFirma("QR");
            firmaElectronica.setPalabraDesde(local.getPalabraDesde());
            firmaElectronica.setPalabraHasta(local.getPalabraHasta());
            firmaElectronica.setMotivo(local.getMotivo());
            firmaElectronica.setMovimientoX(local.getMovimientoX());
            firmaElectronica.setMovimientoY(local.getMovimientoY());
            firmaElectronica.setUbicacion(Constantes.ubicacion);
            firmaElectronica.setAliasToken(local.getToken());

            FirmaDocDesk firmaDocDesk = new FirmaDocDesk();
            firmaDocDesk.setArchivo(target.getAbsolutePath());
            firmaDocDesk.setClave(local.getClave());
            firmaDocDesk.setTipoDesk(local.getTipoDesk());

            if (firmaDocDesk.getTipoDesk().equals("TOKEN")) {
                firmaElectronica = firmarDocumentoToken(firmaElectronica);
            } else {
                firmaElectronica = firmarDocumentoArchivo(firmaElectronica, Boolean.TRUE);
            }


            if (Utils.isNotEmptyString(firmaElectronica.getArchivoFirmado())) {
                byte[] docByteArry = DocumentoUtils.loadFile((firmaElectronica.getArchivoFirmado()));
                if (docByteArry != null) {
                    try {
                        if (local.getModulo() != null) {
                            String path = "";
                            switch (local.getModulo()) {
                                case "ADMINISTRATIVO":
                                    path = administrativo;
                                    break;
                                case "DOCUMENTAL":
                                    path = calidad;
                                    break;
                                case "NOTIFICACION":
                                    path = notificacion;
                                    break;
                                case "BDDIMI":
                                    path = bddimi;
                                    break;
                                case "VENTANILLA":
                                    path = ventanilla;
                                    break;
                            }

                            log.info("path: " + path);
                            local.setArchivoFirmadoB64(Base64.getEncoder().encodeToString(docByteArry));
                            //local.setTipo(null);
                            System.out.println("local: " + local.getArchivoFirmadoB64().length());
                            HttpEntity<Object> request = new HttpEntity<>(local, headers);
                            ResponseEntity<FirmaDocumento> response = restTemplate.exchange(path, HttpMethod.POST, request, FirmaDocumento.class);
                            if (response.hasBody()) {
                                FirmaDocumento dl = response.getBody();
                                if (!local.getDocumento().equals(dl.getDocumento())) {
                                    local.setEstadoFirmado("OK");
                                } else {
                                    local.setEstadoFirmado("Intente nuevamente");
                                }
                            }
                        } else {
                            local.setEstadoFirmado("No existe conexión con ORIGAMIGT");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    local.setEstadoFirmado("El documento no pudo ser firmado");
                }
            } else {
                local.setEstadoFirmado("El documento no pudo ser firmado");
            }

            if (is != null) {
                IOUtils.closeQuietly(is);
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (target != null) {
               /* try {
                    FileDeleteStrategy.FORCE.delete(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
            return local;
            //return firmaElectronica;
        } catch (Exception e) {
            if (e != null && e.getMessage() != null && e.getMessage().contains("keystore password was incorrect")) {
                local.setEstadoFirmado("Clave incorrecta");
            } else {
                local.setEstadoFirmado("Intente nuevamente");
            }
            e.printStackTrace();
        }

        return local;

    }

    public FirmaDocumento subirDocumento(FirmaDocumento local) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if (local.getSubirArchivo() != null && !local.getSubirArchivo().isEmpty()) {
                byte[] docByteArry = DocumentoUtils.loadFile((local.getSubirArchivo()));
                if (docByteArry != null) {

                    local.setArchivoFirmado(Base64.getEncoder().encodeToString(docByteArry));
                    HttpEntity<Object> request = new HttpEntity<>(local, headers);
                    ResponseEntity<FirmaDocumento> response = restTemplate.exchange(origamiGT + Constantes.apiArchivos + "actualizarDocumento", HttpMethod.POST, request, FirmaDocumento.class);
                    if (response.hasBody()) {
                        FirmaDocumento dl = response.getBody();
                        if (!local.getDocumento().equals(dl.getDocumento())) {
                            local.setEstado("OK");
                        } else {
                            local.setEstado("Intente nuevamente");
                        }
                    } else {
                        local.setEstado("No existe conexión con ORIGAMIGT");
                    }
                } else {
                    local.setEstado("El documento no pudo ser firmado");
                }
            } else {
                local.setEstado("Debe escoger el archivo a subir");
            }

            return local;
            //return firmaElectronica;
        } catch (Exception e) {
            if (e != null && e.getMessage() != null && e.getMessage().contains("keystore password was incorrect")) {
                local.setEstado("Clave incorrecta");
            } else {
                local.setEstado("Intente nuevamente");
            }
            e.printStackTrace();
        }

        return local;

    }

    @Async
    public void guardarDocumentoFirmado(FirmaElectronica documentoFirmado, String rutaDocFirmado) {
        log.info("ruta " + origamiGT.concat(Constantes.apiDocumentos));
        try {
            /*Path path = Paths.get(rutaDocFirmado);
            byte[] arr = Files.readAllBytes(path);
            documentoFirmado.setArchivoDesk(arr);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(documentoFirmado, headers);

            ResponseEntity<FirmaDocumento> response = restTemplate.exchange(origamiGT + Constantes.apiDocumentos, HttpMethod.POST, request, FirmaDocumento.class);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        return (osName.toUpperCase().indexOf("LINUX") == 0);
    }


    public static byte[] firmar(KeyStore keyStore, String alias, File documento, char[] clave, Properties params) throws Exception {
        byte[] docByteArry = FileUtils.fileConvertToByteArray(documento);
        Signer signer = Utils.documentSigner(documento);
        PrivateKey key = null;
        try {
            key = (PrivateKey) keyStore.getKey(alias, clave);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Certificate[] certChain = keyStore.getCertificateChain(alias);
        if (key != null) {
            try {
                return signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, key, certChain, params);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void abrirDocumento(String documento) {
        try {
            FileUtils.abrirDocumento(documento);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static int diasRestantes(Date fecha) {
        DateFormat dd = new SimpleDateFormat("dd/MM/yyyy");
        int dias = 0;
        boolean activo = false;
        Calendar calendar;
        Date aux;
        do {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, dias);
            aux = calendar.getTime();
            if (dd.format(aux).equals(dd.format(fecha)))
                activo = true;
            else
                dias++;
        } while (!activo);
        return dias;
    }

    public static Integer[] getFontPosition(String filePath, final String palabra) throws IOException {
        final Integer[] result = new Integer[3];
        File f = new File(filePath);
        com.itextpdf.text.pdf.PdfReader pdfReader = new com.itextpdf.text.pdf.PdfReader(filePath);
        Integer pagina = pdfReader.getNumberOfPages();

        Boolean vertical;
        Rectangle rectangle = pdfReader.getPageSizeWithRotation(pagina);
        Integer height = Math.round(rectangle.getHeight());
        System.out.println("rectangle.getWidth(): " + rectangle.getWidth());
        System.out.println("rectangle.getHeight(): " + rectangle.getHeight());
        System.out.println("pagina: " + pagina);
        try {
            if (rectangle.getHeight() >= rectangle.getWidth())
                vertical = Boolean.TRUE;
            else
                vertical = Boolean.FALSE;
            new PdfReaderContentParser(pdfReader).processContent(pagina, new RenderListener() {
                @Override
                public void beginTextBlock() {
                }

                @Override
                public void renderText(TextRenderInfo textRenderInfo) {
                    String text = textRenderInfo.getText();
                    if (text != null && text.contains(palabra)) {
                        com.itextpdf.awt.geom.Rectangle2D.Float textFloat = textRenderInfo.getBaseline().getBoundingRectange();
                        float x = textFloat.x;
                        float y = textFloat.y;
                        result[0] = (int) x;
                        result[1] = (int) y - 10;

                        LineSegment baseline = textRenderInfo.getBaseline();
                        float x1 = baseline.getStartPoint().get(0);
                        float y2 = baseline.getStartPoint().get(1);
                        float w = baseline.getLength();
                        String txt = textRenderInfo.getText();
                        log.info("Text: @({" + x1 + "}, {" + y2 + "}) width: {" + w + "} txt: {" + txt + "}");

                    }
                }

                @Override
                public void endTextBlock() {

                }

                @Override
                public void renderImage(ImageRenderInfo renderInfo) {

                }
            });
            if (!vertical) {
                System.out.println("es horizontal");
                Integer x = result[0];
                Integer y = result[1];
                System.out.println("y: " + y);
                result[0] = y;
                result[1] = Math.abs(height - x); //SI MUEVES ESTO2 MUERES
            }
            result[2] = pagina;
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            result[0] = 70;
            result[1] = 70;
            result[2] = pagina;
        }
        System.out.println("palabra: " + palabra);
        System.out.println("result[0]: " + result[0]);
        System.out.println("result[1]: " + result[1]);
        System.out.println("result[2]: " + result[2]);
        pdfReader.close();

        if (result[0] == null) { //NO SE PUDO ENCONTRAR LA PALABRA XK SIEMPRE LA BUSCA EN LA ULTIMA PAGINA AHORA VA Y BUSCA EN TODO EL DOCUMUENTO
            System.out.println("se buscca en todo  el documento");
            Integer[] printSubwords = new SearchSubword().printSubwords(PDDocument.load(new File(filePath)), palabra);
            result[0] = printSubwords[0];
            result[1] = printSubwords[1];
            result[2] = printSubwords[2];
            System.out.println("printSubwords[0]: " + printSubwords[0]);
            System.out.println("printSubwords[1]: " + printSubwords[1]);
            System.out.println("printSubwords[2]: " + printSubwords[2]);
        }

      /*  Integer[] posicion = PDFTextLocator.getCoordiantes(PDDocument.load(new File(filePath)), palabra, pagina - 1);
        System.out.println("float[] posicion [0]: " + posicion[0]);
        System.out.println("float[] posicion [1]: " + posicion[1]);
        result[0] =  posicion[0];
        result[1] =  posicion[1];*/


        return result;
    }

    public ObjectMapper mapperDate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public FirmaElectronica uploadFirma(FirmaElectronica data) {
        String name = Utils.getValorUuid();
        String path = rutaArchivos;
        try {
            // Crea los directorios si no existen
            Files.createDirectories(Paths.get(path));
            String fileName = name;
            Path filePath = Paths.get(path, fileName);
            InputStream inputStream = byteArrayToInputStream(data.getFileByte());
            Files.copy(inputStream, filePath);
            data.setArchivo(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public InputStream byteArrayToInputStream(byte[] byteArray) {
        // Crea un InputStream a partir del arreglo de bytes
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        return inputStream;
    }

}
