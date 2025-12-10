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
package org.sas.firmaec.rubrica.certificate.ec.cj;

import java.security.cert.X509Certificate;

import static org.sas.firmaec.rubrica.utils.BouncyCastleUtils.certificateHasPolicy;

/**
 * Permite construir certificados tipo CertificadoConsejoJudicatura a partir de
 * certificados X509Certificate.
 *
 * @author mfernandez
 */
public class CertificadoConsejoJudicaturaDataFactory {

    public static boolean esCertificadoDelConsejoJudicatura(X509Certificate certificado) {
        return (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_NATURAL)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_JURIDICA_PRIVADA)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_JURIDICA_PUBLICA)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_MIEMBRO_EMPRESA)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_EMPRESA)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_DEPARTAMENTO_EMPRESA)
                || certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_SELLADO_TIEMPO));
    }

    public static CertificadoConsejoJudicatura construir(X509Certificate certificado) {
        if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_NATURAL)) {
            return new CertificadoPersonaNaturalConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_JURIDICA_PRIVADA)) {
            return new CertificadoPersonaJuridicaPrivadaConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_PERSONA_JURIDICA_PUBLICA)) {
            return new CertificadoPersonaJuridicaPublicaConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_MIEMBRO_EMPRESA)) {
            return new CertificadoMiembroEmpresaConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_EMPRESA)) {
            return new CertificadoEmpresaConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_CERTIFICADO_DEPARTAMENTO_EMPRESA)) {
            return new CertificadoDepartamentoEmpresaConsejoJudicatura(certificado);
        } else if (certificateHasPolicy(certificado, CertificadoConsejoJudicatura.OID_SELLADO_TIEMPO)) {
            return new CertificadoPersonaNaturalConsejoJudicatura(certificado);
        } else {
            throw new RuntimeException("Certificado del Consejo de la Judicatura de tipo desconocido!");
        }
    }
}
