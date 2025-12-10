package org.sas.seguridad.service;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EncryptDecrypt {

    public String encriptarTexto(String texto) {
        String sha256hex = Hashing.sha256()
                .hashString(texto, StandardCharsets.UTF_8)
                .toString();
        return sha256hex;
    }

}
