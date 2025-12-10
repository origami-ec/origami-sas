package org.sas.mail.services;

import org.apache.commons.io.FilenameUtils;
import org.sas.mail.entity.Correo;
import org.sas.mail.entity.CorreoArchivo;
import org.sas.mail.config.ApplicationProperties;
import org.sas.mail.entity.CorreoSettings;
import org.sas.mail.model.CorreoDTO;
import org.sas.mail.repository.CorreoRepository;
import org.sas.mail.utils.Constantes;
import org.sas.mail.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.*;

@Service
public class CorreoReferenciaService {
    private final String contentType = CTYPE_TEXT;

    private static final String CTYPE_TEXT = "text/html; charset=utf-8";
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CorreoRepository repository;

    public CorreoDTO enviarCorreo(CorreoDTO correoDto) {
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
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.timeout", "60000");
            props.setProperty("mail.smtp.connectiontimeout", "60000");
            //props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.socketFactory.fallback", "true");
            props.setProperty("mail.smtp.starttls.required", "true");
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

            return saveCorreoAndCorreoArchivo(correoDto, Boolean.TRUE);
            //} catch (MessagingException | IOException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
            return saveCorreoAndCorreoArchivo(correoDto, Boolean.FALSE);
        }
    }


    private CorreoSettings getCorreoSettings(String correoModulo) {
        if (EmailUtils.isEmptyString(correoModulo)) {
            correoModulo = Constantes.correoNotificacionERP;
        }
        switch (correoModulo) {
            case Constantes.correoNotificacionERP:
                System.out.println("correoModulo>>" + correoModulo + " correo >>" + applicationProperties.getCorreoParametros().toString());
                return applicationProperties.getCorreoParametros();
            case Constantes.correoCartera:
                return applicationProperties.getCorreoCartera();
            default:
                return applicationProperties.getCorreoParametros();

        }

    }

    public CorreoDTO saveCorreoAndCorreoArchivo(CorreoDTO correoDTO, Boolean enviado) {
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
        correoDTO.setEnviado(enviado);
        repository.save(correo);
        return correoDTO;
    }

}
