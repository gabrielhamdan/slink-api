package com.hamdan.slinkapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QrCodeService {

    public static final int WIDTH_HEIGHT = 400;

    public String generateQrCode(String slink, Integer widthHeight) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (widthHeight == null || widthHeight.equals(0))
                widthHeight = WIDTH_HEIGHT;

            var matrix = new MultiFormatWriter().encode(slink, BarcodeFormat.QR_CODE, widthHeight, widthHeight);
            var img = MatrixToImageWriter.toBufferedImage(matrix);

            ImageIO.write(img, "PNG", baos);

            baos.flush();

            byte[] byteArray = baos.toByteArray();

            return Base64.getEncoder().encodeToString(byteArray);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
