package org.ibarra.conf;


import org.ibarra.dto.RespuestaWs;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.util.logging.Level;
import java.util.logging.Logger;


@ControllerAdvice
public class GlobalHandler {
    static Logger LOG = Logger.getLogger(GlobalHandler.class.getSimpleName());

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<RespuestaWs> hendlerException(Exception e) {
        RespuestaWs respuestaWs = new RespuestaWs(false, e.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof HttpMessageNotReadableException) {
            status = HttpStatus.ACCEPTED;
            respuestaWs.setMensaje("Cuerpo del detalle es requerido: " + e.getMessage());
        } else if (e instanceof DataIntegrityViolationException) {
            status = HttpStatus.ACCEPTED;
            respuestaWs.setMensaje("Validacion de datos de modelo de datos: " + e.getMessage());
        } else {
            respuestaWs.setMensaje("Error interno del servidor: " + e.getMessage());
        }
        LOG.log(Level.SEVERE, "Error interno del servidor Type: " + e.getClass() + " Message: " + e.getMessage(), e);
        return ResponseEntity.status(status).body(respuestaWs);
    }

    //    @ControllerAdvice
    static
    class CustomAdvice {
        @ExceptionHandler(AsyncRequestNotUsableException.class)
        void handleAsyncRequestNotUsableException(AsyncRequestNotUsableException e) {
            LOG.log(Level.SEVERE, "Please ignore below error, because it's inner issue in SB,", e);
        }
    }
}
