package org.sas.gateway.service;

import org.sas.gateway.models.SessionCloseMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionCloseListener {
    @Autowired
    private RuteoModuloService ruteoModuloService;

    @RabbitListener(queues = "session.close.queue")
    public void handleSessionClose(SessionCloseMessage message) {
        try {
            System.out.println("Received session close request for module: {}" + message.getModuleName());
            ruteoModuloService.handleSessionClose(message);
        } catch (Exception e) {
            System.out.println("Failed to handle session close for module: " + message.getModuleName());
        }
    }
}
