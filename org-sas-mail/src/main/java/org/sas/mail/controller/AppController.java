package org.sas.mail.controller;

import org.sas.mail.config.ApplicationProperties;
import org.sas.mail.model.CorreoArchivoDTO;
import org.sas.mail.model.CorreoDTO;
import org.sas.mail.model.RespuestaWs;
import org.sas.mail.services.CorreoService;
import org.sas.mail.utils.EmailUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AppController {

    private Logger logger = Logger.getLogger(AppController.class.getName());

    @Autowired
    private CorreoService correoLocalService;

    @Autowired
    private ApplicationProperties appProps;


    @PostMapping("enviarCorreo")
    public ResponseEntity<?> enviarCorreo(@RequestBody CorreoDTO correoDTO) {
        try {
            if (correoDTO.getCorreoModulo() == null) {
                correoDTO.setCorreoModulo("NOTIFICACION");
            }
            logger.log(Level.INFO, "enviarCorreo " + correoDTO.getCorreoModulo());
            if (EmailUtils.isNotEmpty(correoDTO.getArchivos())) {
                correoDTO.setAdjuntos(convertToFiles(correoDTO.getArchivos()));
            } else {
                if (EmailUtils.isEmpty(correoDTO.getAdjuntos())) {
                    correoDTO.setAdjuntos(null);
                }

            }
            if (correoDTO.getDestinatario() != null && !correoDTO.getDestinatario().isEmpty()) {
                correoDTO.setMensaje(correoLocalService.mailHtmlNotificacion(correoDTO));
                String[] destinatario = correoDTO.getDestinatario().split("\\;");
                if (destinatario.length > 1) {
                    for (String s : destinatario) {
                        logger.info("destinatario: " + s);
                        correoDTO.setDestinatario(s);
                        if (appProps.isDev()) {
                            correoLocalService.enviarCorreo(correoDTO);
                        } else {
                            switch (correoDTO.getCorreoModulo()) {
                                case "NOTIFICACION":
                                    correoLocalService.enviarCorreo(correoDTO);
                                    break;
                                case "VENTANILLA":
                                default:
                                    correoLocalService.enviarCorreo(correoDTO);
                                    break;
                            }
                        }

                    }
                } else {
                    destinatario = correoDTO.getDestinatario().split(",");
                    if (destinatario.length > 1) {
                        for (String s : destinatario) {
                            logger.info("destinatario: " + s);
                            correoDTO.setDestinatario(s);
                            if (appProps.isDev()) {
                                correoLocalService.enviarCorreo(correoDTO);
                            } else {
                                switch (correoDTO.getCorreoModulo()) {
                                    case "NOTIFICACION":
                                        correoLocalService.enviarCorreo(correoDTO);
                                        break;
                                    case "VENTANILLA":
                                    default:
                                        correoLocalService.enviarCorreo(correoDTO);
                                        break;
                                }
                            }
                        }
                    } else {
                        logger.info("destinatario: " + correoDTO.getDestinatario());
                        if (appProps.isDev()) {
                            correoLocalService.enviarCorreo(correoDTO);
                        } else {
                            //correoService.enviarCorreo(correoDTO);
                            correoLocalService.enviarCorreo(correoDTO);
                        }
                    }

                }
                return new ResponseEntity<>(new RespuestaWs(Boolean.TRUE, "OK"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RespuestaWs(Boolean.FALSE, "DESTINATARIO VACIO"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new RespuestaWs(Boolean.FALSE, "INTENTE NUEVAMENTE"), HttpStatus.NO_CONTENT);
        }

    }


    private List<File> convertToFiles(List<CorreoArchivoDTO> correoArchivoDTOS) {
        List<File> files = new ArrayList<>();
        File file;
        for (CorreoArchivoDTO correoArchivoDTO : correoArchivoDTOS) {
            System.out.println(correoArchivoDTO.getNombreArchivo());
            file = new File(EmailUtils.replaceRutaArchivo(correoArchivoDTO.getNombreArchivo(), appProps.getRutaArchivos()));
            files.add(file);
        }
        return files;
    }

}
