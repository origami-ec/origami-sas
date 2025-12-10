package org.sas.mail.utils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class EmailUtils {

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !isEmpty(l);
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !EmailUtils.isEmptyString(l);
    }

    public static String replaceRutaArchivo(String ruta, String replace) {
        if (ruta.startsWith("ar_")) {
            System.out.println(ruta);
            ruta = ruta.replaceFirst("ar_", replace);
        }
        return ruta;
    }

    public static Date getFechaMongo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Configuramos la fecha DE HOY
        calendar.add(Calendar.HOUR, -5);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }
}
