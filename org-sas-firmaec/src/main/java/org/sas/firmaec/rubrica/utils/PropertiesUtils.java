/*
 * Copyright (C) 2020
 * Authors: Ricardo Arguello, Misael Fernández
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.sas.firmaec.rubrica.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilidad para menejar properties
 *
 * @author mfernandez
 */
public class PropertiesUtils {

    private static final String MESSAGES = "classpath:messagesrubrica.properties";
    private static final String CONFIG = "classpath:configrubrica.properties";
    private static Properties messages;
    private static Properties config;

    public static Properties getMessages() {
        messages = new Properties();
        try {
            String OS = System.getProperty("os.name").toLowerCase();

            if (OS.contains("win")) {
                // Cargar desde el classpath dentro del JAR
                InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("messages.properties");
                if (inputStream != null) {
                    messages.load(inputStream);
                } else {
                    throw new FileNotFoundException("El archivo messages.properties no se encontró en el classpath.");
                }
            } else {
                // Cargar desde el sistema de archivos si no es Windows
                File file = ResourceUtils.getFile(MESSAGES);
                messages.load(new FileInputStream(file));
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return messages;
    }
    public static Properties getConfig() {
        config = new Properties();
        try {
            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.contains("win")) {
                InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("configrubrica.properties");
                if (inputStream != null) {
                    config.load(inputStream);
                }else{
                    throw new FileNotFoundException("El archivo config.properties no se encontró en el classpath.");
                }

                //  File file = ResourceUtils.getFile(CONFIG);
                /// config.load(new FileInputStream(file));
            } else {
                config.load(new ClassPathResource(CONFIG).getInputStream());
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return config;
    }

}
