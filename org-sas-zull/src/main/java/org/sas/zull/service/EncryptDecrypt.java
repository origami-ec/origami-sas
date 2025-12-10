package org.sas.zull.service;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EncryptDecrypt {

    public String encriptarClave(String clave) {
        String sha256hex = Hashing.sha256()
                .hashString(clave, StandardCharsets.UTF_8)
                .toString();
        return sha256hex;
    }

}
