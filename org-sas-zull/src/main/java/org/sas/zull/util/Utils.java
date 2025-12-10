package org.sas.zull.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static String getMac(String ip) {
        // Find OS and set command according to OS
        Pattern macpt = null;
        String OS = System.getProperty("os.name").toLowerCase();
        String[] cmd;
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern.compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            String[] a = {"arp", "-a", ip};
            cmd = a;
        } else {
            // Mac OS X, Linux
            macpt = Pattern.compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
            String[] a = {"arp", ip};
            cmd = a;
        }
        try {
            // Run command
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            // read output with BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            // Loop trough lines
//            System.out.println("Line " + line);
            while (line != null) {
                Matcher m = macpt.matcher(line);
                // when Matcher finds a Line then return it as result
                if (m.find()) {
                    //System.out.println("MAC: " + m.group(0));

                    return m.group(0);
                }
                line = reader.readLine();
            }
            if (line != null) {

                return line.toUpperCase();
            }
            return "";
        } catch (IOException | InterruptedException e1) {
            logger.log(Level.SEVERE, "", e1);
            return "";
        }
        // Return empty string if no MAC is found

    }

}

