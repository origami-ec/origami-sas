package org.ibarra.media.util;

import org.ibarra.media.config.AppProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Utils {

    @Autowired
    private AppProps appProps;

    public String replaceRutaArchivo(String ruta) {
        System.out.println(ruta);
        if (ruta.startsWith("fd_")) {
            ruta = ruta.replaceFirst("fd_", appProps.getArchivoFirmados());
        } else if (ruta.startsWith("ar_")) {
            ruta = ruta.replaceFirst("ar_", appProps.getArchivos());    //Informacion para iniciar signed.pdf ar_Informacion_para_iniciar_signed.pdf
        } else if (ruta.startsWith("imgtemp_")) {
            ruta = ruta.replaceFirst("imgtemp_", appProps.getImagenes());
        } else if (ruta.startsWith("doc_")) {
            ruta = ruta.replaceFirst("doc_", appProps.getDocumental());
        } else if (ruta.startsWith("not_")) {
            ruta = ruta.replaceFirst("not_", appProps.getNotas());
        } else if (ruta.startsWith("ma_")) {
            ruta = ruta.replaceFirst("ma_", appProps.getManuales());
        }else if (ruta.startsWith("th_")) {
            ruta = ruta.replaceFirst("th_", appProps.getTalentoHumanoFormato());
        } else {
            ruta = appProps.getArchivos() + ruta;
        }

        return ruta;
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void crearDirectorio(String directory) {
        try {
            if (directory != null) {
                Path path = Paths.get(directory.endsWith("/") ? directory : directory.concat("/"));
                Path parent = path.getParent();
                if (parent != null) {
                    if (parent.toFile().exists()) {
                        if (!path.toFile().exists()) {
                            Files.createDirectory(path);
                            System.out.println("Create directory:" + path);
                        } else {
                            System.out.println("Exists directory " + directory);
                        }
                    } else {
                        Files.createDirectories(path);
                    }
                } else {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "No se pudo crear el directorio: {0}", ex.getMessage());
        }
    }
}
