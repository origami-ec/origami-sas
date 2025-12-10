package org.sas.zull.conf;

import org.sas.zull.entity.Logs;
import org.sas.zull.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogServices {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLogServices.class);

    @Value("${spring.profiles.active}")
    private String perfilActivo;
    @Autowired
    private RestService restService;

    @Async
    public void sendMessageLogFile(String message) {
        if (perfilActivo.equals("prod") || perfilActivo.equals("pre")) {
            logger.info(String.format("#### -> Mensaje -> %s", message));
            try {
                //this.kafkaTemplate.send(topics[0], message);
            } catch (Exception e) {
                System.out.println("Error al enviar los kafkaTemplate");
            }
        }
    }


    @Async
    public void sendMessageLogAsgard(Logs logs) {
        if (perfilActivo.equals("prod") || perfilActivo.equals("pre")) {
            //  logger.info(String.format("#### -> Mensaje -> %s", message));
            try {
                String url = "http://172.16.8.138:8717/logs/guardar";
                restService.restPOST(url, null, logs, Logs.class);
                //this.kafkaTemplate.send(topics[0], message);
            } catch (Exception e) {
                System.out.println("Error al enviar los asgar kafkaTemplate");
            }
        }
    }

}
