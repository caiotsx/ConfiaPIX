package com.confiapix.validador_pix.Parser;

import java.util.Map;

public interface ComprovanteParser {
    boolean identificaBanco(String textoOCR);
    Map<String, String> extrairCampos(String textoOCR);
}