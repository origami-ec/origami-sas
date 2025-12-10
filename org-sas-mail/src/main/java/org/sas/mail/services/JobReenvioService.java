package org.sas.mail.services;

import org.sas.mail.entity.Correo;
import org.sas.mail.repository.CorreoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JobReenvioService {

    @Autowired
    private CorreoRepository repository;
    @Autowired
    private BrevoEmailService correoService;

   // @Scheduled(cron = "0 0/10 * * * ?")
    public void reenvio() {
        System.out.println("reenvio");
        List<Correo> correos = obtenerCorreosPorRangoFecha();

        int contador = 0;

        for (Correo correo : correos) {
                contador++;
                System.out.println("asunto: " + correo.getAsunto());
                System.out.println("Reenviando correo: " + correo.getDestinatario() +
                        " | Correos reenviados hasta ahora: " + contador  + " | Correos totales: " + correos.size());

                correoService.reenviarCorreo(correo);
                correo.setReenviado(true);
                repository.save(correo);

        }
        System.out.println("Total de correos reenviados: " + contador);
    }


    public List<Correo> obtenerCorreosPorRangoFecha() {
        try {

            // Crear el formato de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // Fecha de inicio (16 de junio)
            Date startDate = sdf.parse("2025-06-16");

            // Fecha de fin (21 de junio)
            Date endDate = sdf.parse("2025-06-21");

            // Obtener los correos en el rango de fechas
            return repository.findByFechaEnvioBetweenAndReenviadoAndDestinatarioAndAsunto(startDate, endDate);


        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


}
