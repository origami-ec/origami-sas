package org.sas.seguridad.conf;

import org.sas.seguridad.entity.CorreoFormato;
import org.sas.seguridad.entity.Valor;
import org.sas.seguridad.service.CorreoFormatoService;
import org.sas.seguridad.service.ValorService;
import org.sas.seguridad.util.ValoresCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ApplicationStartup implements ApplicationRunner {
    private static final Logger logger = Logger.getLogger(ApplicationStartup.class.getName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private ValorService valorService;
    @Autowired
    private CorreoFormatoService correoFormatoService;

    @Override
    public void run(ApplicationArguments var1) {
        logger.info("Iniciando parametros");
        Valor valor = valorService.findByCode(ValoresCodigo.TIEMPO_VALIDO);
        if (valor != null) {
            appProps.setCodigoTiempo(valor.getValorNumeric().intValue());
        }
        valor = valorService.findByCode(ValoresCodigo.CODIGO_SHA);
        if (valor != null) {
            appProps.setCodigoSha(valor.getValorString());
        }
        valor = valorService.findByCode(ValoresCodigo.CODIGO_VERIFICACION_LONGITUD);
        if (valor != null) {
            appProps.setCodigoLongitud(valor.getValorNumeric().intValue());
        }
        valor = valorService.findByCode(ValoresCodigo.USUARIO_ACTIVO);
        if (valor != null) {
            appProps.setUsuarioActivo(valor.getValorString());
        }
        valor = valorService.findByCode(ValoresCodigo.USUARIO_INACTIVO);
        if (valor != null) {
            appProps.setUsuarioInactivo(valor.getValorString());
        }
        valor = valorService.findByCode(ValoresCodigo.USUARIO_PENDIENTE);
        if (valor != null) {
            appProps.setUsuarioPendiente(valor.getValorString());
        }
        valor = valorService.findByCode(ValoresCodigo.USUARIO_BLOQUEADO);
        if (valor != null) {
            appProps.setUsuarioBloqueado(valor.getValorString());
        }

        /*
        Variables de corrreo
         */
        CorreoFormato correoFormato = correoFormatoService.getCorreoFormato(ValoresCodigo.ACTIVACION_USUARIO);
        if (correoFormato != null) {
            appProps.setActivarUsuarioAsunto(correoFormato.getAsunto());
            appProps.setActivarUsuarioMensaje(correoFormato.getMensaje());
            appProps.setActivarUsuarioVinculo(correoFormato.getTextoVinculo());
        }
        correoFormato = correoFormatoService.getCorreoFormato(ValoresCodigo.ACTUALIZAR_CREDENCIALES);
        if (correoFormato != null) {
            appProps.setActualizarUsuarioAsunto(correoFormato.getAsunto());
            appProps.setActualizarUsuarioMensaje(correoFormato.getMensaje());
            appProps.setActualizarUsuarioVinculo(correoFormato.getTextoVinculo());
        }

        correoFormato = correoFormatoService.getCorreoFormato(ValoresCodigo.DOBLE_VERIFICACION);
        if (correoFormato != null) {
            appProps.setActualizarDobleVerificacionAsunto(correoFormato.getAsunto());
            appProps.setActualizarDobleVerificacionMensaje(correoFormato.getMensaje());
        }
        logger.info("Parametros cargados");

    }

}
