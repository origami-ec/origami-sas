/*
 * Copyright (C) 2023
 * Authors: Steven Chiriboga
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
package org.sas.firmaec.rubrica.certificate.ec.alphatechnologies;

import org.sas.firmaec.rubrica.exceptions.EntidadCertificadoraNoValidaException;
import org.sas.firmaec.rubrica.utils.BouncyCastleUtils;

import java.security.cert.X509Certificate;

/**
 * Permite construir certificados tipo {@link CertificadoAlphaTechnologiesFactory} a partir de
 * certificados X509Certificate.
 *
 * 
 */
public class CertificadoAlphaTechnologiesFactory {
    
    public static boolean esCertificadoDeAlphaTechnologies(X509Certificate certificado) {
        return (BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_PERSONA_NATURAL)
                || BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_PERSONA_JURIDICA)
                || BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_MIEMBRO_EMPRESA));
    }

    public static CertificadoAlphaTechnologies construir(X509Certificate certificado) throws EntidadCertificadoraNoValidaException {
        if (BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_PERSONA_NATURAL)) {
            return new CertificadoPersonaNaturalAlphaTechnologies(certificado);
        } else if (BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_PERSONA_JURIDICA)) {
            return new CertificadoPersonaJuridicaAlphaTechnologies(certificado);
        } else if (BouncyCastleUtils.certificateHasPolicy(certificado, CertificadoAlphaTechnologies.OID_CERTIFICADO_MIEMBRO_EMPRESA)) {
            return new CertificadoMiembroEmpresaAlphaTechnologies(certificado);
        } else {
            throw new EntidadCertificadoraNoValidaException("Certificado de Alpha Technologies Cia. Ltda. sin categorizar!");
        }
    }
    
}
