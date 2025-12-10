package org.sas.administrativo.conf;

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

    @Override
    public void run(ApplicationArguments var1) {
        logger.info("Iniciando parametros");
        logger.info("Parametros cargados");

    }

}
