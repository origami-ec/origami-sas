package org.origami.docs.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.origami.docs.dto.QRCodeRequest;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

@Service
public class QRCodeService {

    /**
     * Genera un código QR con texto adicional como título y pie de página.
     *
     * @param qrCodeRequest Objeto que contiene los datos para generar el código QR.
     * @return Un arreglo de bytes que representa la imagen PNG.
     * @throws WriterException Si ocurre un error al generar el código QR.
     * @throws IOException     Si ocurre un error al procesar la imagen.
     */
    public byte[] generateQRCodeWithText(QRCodeRequest qrCodeRequest)

            throws WriterException, IOException {

        String text = qrCodeRequest.getText();
        String title = qrCodeRequest.getTitle();
        String footer = qrCodeRequest.getFooter();
        int width = qrCodeRequest.getWidth();
        int height = qrCodeRequest.getHeight();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1); // Margen del código QR (ajustar según sea necesario)

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        // Crear una imagen con fondo transparente
        BufferedImage image = new BufferedImage(width, height + 60, BufferedImage.TYPE_INT_ARGB);  // Se agrega espacio para el título y pie de página
        Graphics2D graphics = image.createGraphics();

        // Hacer que el fondo sea transparente
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(0, 0, 0, 0)); // Color transparente
        graphics.fillRect(0, 0, width, height + 60);  // Se ajusta para dar espacio al título y pie de página

        // Dibujar el título
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 18));
        graphics.drawString(title, (width - graphics.getFontMetrics().stringWidth(title)) / 2, 20);

        // Establecer el color de los píxeles del código QR
        graphics.setColor(Color.BLACK); // Código QR en color negro
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (bitMatrix.get(i, j)) {
                    image.setRGB(i, j + 30, Color.BLACK.getRGB());  // Desplazar QR hacia abajo para espacio del título
                }
            }
        }

        // Dibujar el pie de página
        graphics.setFont(new Font("Arial", Font.BOLD, 24));
        graphics.drawString(footer, (width - graphics.getFontMetrics().stringWidth(footer)) / 2, height + 40);

        graphics.dispose();

        // Convertir la imagen a un arreglo de bytes para devolverla
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
