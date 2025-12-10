package org.sas.zull.conf;

import org.sas.zull.service.RestService;
import org.sas.zull.entity.Valor;
import org.sas.zull.util.Constantes;
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
    private RestService restService;

    @Override
    public void run(ApplicationArguments var1) {
        logger.info("Iniciando parametros");
        appProps.setUrlApp(Constantes.localhost + appProps.getPort());
        Valor valor = consultarValor(Constantes.INTENTOS_SESION);
        if (valor != null) {
            appProps.setIntentosSesion(valor.getValorNumeric().intValue());
        } else {
            appProps.setIntentosSesion(Constantes.INTENTOS_SESION_DEFAULT);
        }
        valor = consultarValor(Constantes.USUARIO_ACTIVO);
        if (valor != null) {
            appProps.setUsuarioActivo(valor.getValorString());
        }
        valor = consultarValor(Constantes.USUARIO_INACTIVO);
        if (valor != null) {
            appProps.setUsuarioInactivo(valor.getValorString());
        }
        valor = consultarValor(Constantes.USUARIO_PENDIENTE);
        if (valor != null) {
            appProps.setUsuarioPendiente(valor.getValorString());
        }
        valor = consultarValor(Constantes.USUARIO_BLOQUEADO);
        if (valor != null) {
            appProps.setUsuarioBloqueado(valor.getValorString());
        }
    }

    private Valor consultarValor(String codigo) {
        try {
            Valor valor = new Valor();
            valor.setCode(codigo);
            String url = appProps.getUrlApp() + "/servicios/sas/api/valor/find";
            valor = (Valor) restService.restPOST(url, null, valor, Valor.class);
            return valor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
