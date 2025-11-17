package com.confiapix.validador_pix.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OcrService {

    public String extrairTexto(File imagem) throws IOException {

        Tesseract tesseract = new Tesseract();

        ClassPathResource tessData = new ClassPathResource("tessdata");
        tesseract.setDatapath(tessData.getFile().getAbsolutePath());

        tesseract.setLanguage("por");

        try {
            return tesseract.doOCR(imagem);
        } catch (TesseractException e) {
            throw new RuntimeException("Erro ao processar OCR: " + e.getMessage());
        }
    }

    // 🆕 Novo método: OCR de PDF
    public String extrairTextoDePdf(File pdfFile) {
        StringBuilder texto = new StringBuilder();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            Tesseract tesseract = new Tesseract();
            tesseract.setLanguage("por");

            int totalPages = document.getNumberOfPages();
            for (int i = 0; i < totalPages; i++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300);
                texto.append(tesseract.doOCR(image)).append("\n");
            }
        } catch (IOException | TesseractException e) {
            throw new RuntimeException("Erro ao processar PDF via OCR: " + e.getMessage());
        }
        return texto.toString();
    }

    @Autowired
    private BancoDetectorService bancoDetectorService;

    public Map<String, String> extrairCampos(String textoOCR) {
        return bancoDetectorService.processarComprovante(textoOCR);
    }

}