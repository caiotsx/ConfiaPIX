package com.confiapix.validador_pix.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.confiapix.validador_pix.Parser.BradescoParser;
import com.confiapix.validador_pix.Parser.ComprovanteParser;
import com.confiapix.validador_pix.Parser.ItauParser;
import com.confiapix.validador_pix.Parser.NubankParser;

@Service
public class BancoDetectorService {

    private final List<ComprovanteParser> parsers;

    public BancoDetectorService() {
        this.parsers = List.of(
            new ItauParser(),
            new NubankParser(),
            new BradescoParser()
            // Adicione mais bancos conforme necessário
        );
    }

    public Map<String, String> processarComprovante(String textoOCR) {
        for (ComprovanteParser parser : parsers) {
            if (parser.identificaBanco(textoOCR)) {
                return parser.extrairCampos(textoOCR);
            }
        }
        // Se não encontrar nenhum banco conhecido:
        Map<String, String> fallback = new HashMap<>();
        fallback.put("erro", "Instituição não reconhecida.");
        return fallback;
    }
}