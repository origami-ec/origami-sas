package org.sas.mail.services;

import org.apache.commons.io.FilenameUtils;
import org.sas.mail.entity.Correo;
import org.sas.mail.entity.CorreoArchivo;
import org.sas.mail.entity.CorreoFormato;
import org.sas.mail.config.ApplicationProperties;
import org.sas.mail.entity.CorreoSettings;
import org.sas.mail.model.CorreoDTO;
import org.sas.mail.repository.CorreoFormatoRepository;
import org.sas.mail.repository.CorreoRepository;
import org.sas.mail.repository.CorreoSettingsRepository;
import org.sas.mail.utils.Constantes;
import org.sas.mail.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CorreoService {
    private final String contentType = CTYPE_TEXT;

    private static final String CTYPE_TEXT = "text/html; charset=utf-8";
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CorreoRepository repository;
    @Autowired
    private CorreoSettingsRepository correoSettingsRepository;
    @Autowired
    private CorreoFormatoRepository correoFormatoRepository;


    public Map<String, List> findAll(Correo data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Correo> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    @Async
    public void reenviarCorreo(Correo correo) {
        List<File> files = new ArrayList<>();
        File file;
        if (correo.getArchivos() != null && !correo.getArchivos().isEmpty()) {
            for (CorreoArchivo correoArchivo : correo.getArchivos()) {
                file = new File(EmailUtils.replaceRutaArchivo(correoArchivo.getRutaArchivo(), applicationProperties.getRutaArchivos()));
                if (file.exists()) {
                    files.add(file);
                } else {
                    System.out.println("archivo no encontrado :C verificar el borrado de archivos");
                }
            }
        }
        enviarCorreo(correo, files);
    }

    @Async
    public void saveCorreoAndCorreoArchivo(CorreoDTO correoDTO, Boolean enviado) {
        if (correoDTO != null) {
            Correo correo = new Correo();
            correo.setReferencia(correoDTO.getReferencia());
            correo.setReferenciaId(correoDTO.getReferenciaId());
            correo.setDestinatario(correoDTO.getDestinatario());
            correo.setMensaje(correoDTO.getMensaje());
            correo.setEnviado(enviado);
            correo.setAsunto(correoDTO.getAsunto());
            correo.setNumeroTramite(correoDTO.getNumeroTramite());
            correo.setRemitente(correoDTO.getRemitente());
            correo.setDestino(correoDTO.getDestino());
            correo.setFechaEnvio(EmailUtils.getFechaMongo());
            correo.setArchivos(new ArrayList<>());
            correo.setTexto(correoDTO.getTexto());
            correo.setVinculo(correoDTO.getVinculo());
            correo.setTextoVinculo(correoDTO.getTextoVinculo());
            correo.setFooterTexto(correoDTO.getFooterTexto());
            if (EmailUtils.isNotEmpty(correoDTO.getAdjuntos())) {
                for (File f : correoDTO.getAdjuntos()) {
                    CorreoArchivo correoArchivo = new CorreoArchivo();
                    correoArchivo.setNombreArchivo(f.getName());
                    correoArchivo.setRutaArchivo(f.getAbsolutePath());
                    correoArchivo.setTipoArchivo(FilenameUtils.getExtension(f.getAbsolutePath()));
                    correo.getArchivos().add(correoArchivo);
                }
            }
            repository.save(correo);
        }
    }

    @Async
    public void actualizarReenvio(Correo correo) {
        repository.save(correo);
    }

    @Async
    public void enviarCorreo(CorreoDTO correoDto) {
        try {
            CorreoSettings bd = getCorreoSettings(correoDto.getCorreoModulo());

            String correo = bd.getCorreo();
            String razonSocial = bd.getRazonSocial();
            String correoClave = bd.getCorreoClave();
            String correoHost = bd.getCorreoHost();
            String correoPort = bd.getCorreoPort();

            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            props.setProperty("mail.smtp.host", correoHost);
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", correoPort);
            props.setProperty("mail.smtp.user", correo);
            props.setProperty("mail.smtp.auth", "false");
            props.setProperty("mail.smtp.timeout", "60000");
            props.setProperty("mail.smtp.connectiontimeout", "60000");
            props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.socketFactory.fallback", "true");
            props.setProperty("mail.smtp.starttls.required", "false");
            props.setProperty("mail.smtp.ssl.trust", "*");

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correo, razonSocial));
            mimeMessage.setSubject(correoDto.getAsunto());
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDto.getDestinatario()));
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(me
            // nsaje);
            texto.setContent(correoDto.getMensaje(), contentType);
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            if (EmailUtils.isNotEmpty(correoDto.getAdjuntos())) {
             //   System.out.println( "Adjuntos: "+ correoDto.getAdjuntos().size());
                MimeBodyPart file;
                for (File f : correoDto.getAdjuntos()) {
                    file = new MimeBodyPart();
                    file.attachFile(f);
                    multipart.addBodyPart(file);
                }
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            Transport transport = session.getTransport("smtp");
            transport.connect(correo, correoClave);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            saveCorreoAndCorreoArchivo(correoDto, Boolean.TRUE);
            //} catch (MessagingException | IOException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
            saveCorreoAndCorreoArchivo(correoDto, Boolean.FALSE);
        }
    }

    @Async
    public void enviarCorreo(Correo correoDB, List<File> adjuntos) {
        try {

            CorreoSettings bd = getCorreoSettings(correoDB.getCorreoModulo());
            String correo = bd.getCorreo();
            String razonSocial = bd.getRazonSocial();
            String correoClave = bd.getCorreoClave();
            String correoHost = bd.getCorreoHost();
            String correoPort = bd.getCorreoPort();

            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            props.setProperty("mail.smtp.host", correoHost);
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", correoPort);
            props.setProperty("mail.smtp.user", correo);
            props.setProperty("mail.smtp.auth", "true");

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correo, razonSocial));
            mimeMessage.setSubject(correoDB.getAsunto());
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDB.getDestinatario()));
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(correoDB.getMensaje(), contentType);
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            if (EmailUtils.isNotEmpty(adjuntos)) {
                MimeBodyPart file;
                for (File f : adjuntos) {
                    file = new MimeBodyPart();
                    file.attachFile(f);
                    multipart.addBodyPart(file);
                }
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            Transport transport = session.getTransport("smtp");
            transport.connect(correo, correoClave);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            correoDB.setEnviado(Boolean.TRUE);
            actualizarReenvio(correoDB);
        } catch (MessagingException | IOException ex) {
            correoDB.setEnviado(Boolean.FALSE);
            actualizarReenvio(correoDB);
        }
//        finally {
//            if (EmailUtils.isNotEmpty(adjuntos)) {
//                for (File f : adjuntos) {
//                    f.delete();
//                }
//            }
//        }
    }

    public CorreoSettings guardarConfiguraciones(CorreoSettings correoSettings) {
        CorreoSettings bd = correoSettingsRepository.findByCorreo(correoSettings.getCorreo());
        if (bd != null)
            correoSettings.setId(bd.getId());
        correoSettingsRepository.save(correoSettings);
        return correoSettings;
    }


    public CorreoFormato getCorreoFormato(CorreoFormato data) {
        return correoFormatoRepository.findOne(Example.of(data)).orElse(null);
    }

    public String mailHtmlNotificacion(CorreoDTO correoDTO) {
        String format = "";
        CorreoFormato correoFormato = correoFormatoRepository.findAll().get(0);
        if (correoFormato != null) {
            format = correoFormato.getEncabezado()
                    + "<center><h1 style=\"font-size:24px;margin:0 0 20px 0;font-family:Montserrat;\">"
                    + correoDTO.getAsunto() + "</h1></center>" +
                    "<p style=\"margin:0 0 12px 0;font-size:16px;line-height:24px;font-family:Montserrat;\">" + (correoDTO.getMensaje() != null ? correoDTO.getMensaje() : correoDTO.getTexto()) + "</p>"
                    + (correoDTO.getVinculo() != null && !correoDTO.getVinculo().isEmpty() ? "<p style=\"margin:0;font-size:16px;line-height:24px;font-family:Montserrat;\"><a href=\""
                    + correoDTO.getVinculo() + "\" style=\"color:#ee4c50;text-decoration:underline;\">"
                    + (correoDTO.getTextoVinculo() != null ? correoDTO.getTextoVinculo() : "") + " </a></p>" : "")
                    + correoFormato.getPieDePagina();
        }
        return format;
    }

    private CorreoSettings getCorreoSettings(String correoModulo) {
        if (EmailUtils.isEmptyString(correoModulo)) {
            correoModulo = Constantes.correoNotificacionERP;
        }
        switch (correoModulo) {
            case Constantes.correoNotificacionERP:
                return applicationProperties.getCorreoParametros();
            case Constantes.correoCartera:
                return applicationProperties.getCorreoCartera();
            case Constantes.correoCoactiva:
                return applicationProperties.getCorreoCoactiva();
            default:
                return applicationProperties.getCorreoParametros();

        }

    }

}
