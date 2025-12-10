package org.sas.mail.config;

import org.sas.mail.utils.Constantes;
import org.sas.mail.repository.CorreoSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationRunner {

    @Autowired
    private CorreoSettingsRepository correoSettingsRepository;
    @Autowired
    private ApplicationProperties properties;

    @Override
    public void run(ApplicationArguments var1) {
        properties.setCorreoParametros(correoSettingsRepository.findByCodigo(Constantes.correoNotificacionERP));
        properties.setCorreoCartera(correoSettingsRepository.findByCodigo(Constantes.correoCartera));
        properties.setCorreoCoactiva(correoSettingsRepository.findByCodigo(Constantes.correoCoactiva));
    }

}
