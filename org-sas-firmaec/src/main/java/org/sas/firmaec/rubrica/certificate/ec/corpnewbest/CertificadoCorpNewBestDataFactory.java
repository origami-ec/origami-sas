/*
 * Copyright (C) 2023
 * Authors: Pedro Reyes
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
package org.sas.firmaec.rubrica.certificate.ec.corpnewbest;

import org.sas.firmaec.rubrica.exceptions.EntidadCertificadoraNoValidaException;

import java.security.cert.X509Certificate;

import static org.sas.firmaec.rubrica.certificate.ec.corpnewbest.CertificadoCorpNewBest.*;
import static org.sas.firmaec.rubrica.utils.BouncyCastleUtils.certificateHasPolicy;

/**
 * Permite construir certificados tipo Certificado CorpNewBest a partir de
 * certificados X509Certificate.
 *
 * @author Pedro Reyes
 */
public class CertificadoCorpNewBestDataFactory {

    public static boolean esCertificadoCorpNewBest(X509Certificate certificado) {
        byte[] valor = certificado.getExtensionValue(OID_CEDULA_PASAPORTE);
        return (valor != null);
    }

    public static CertificadoCorpNewBest construir(X509Certificate certificado) throws EntidadCertificadoraNoValidaException {
        if (certificateHasPolicy(certificado, OID_TIPO_PERSONA_NATURAL)) {
            return new CertificadoPersonaNaturalCorpNewBest(certificado);
        } else if (certificateHasPolicy(certificado, OID_TIPO_PERSONA_JURIDICA)) {
            return new CertificadoPersonaJuridicaCorpNewBest(certificado);
        } else if (certificateHasPolicy(certificado, OID_TIPO_MIEMBRO_EMPRESA)) {
            return new CertificadoMiembroEmpresaCorpNewBest(certificado);
        } else {
            throw new EntidadCertificadoraNoValidaException("Tipo Certificado de CorpNewBest desconocido!");
        }
    }
}
