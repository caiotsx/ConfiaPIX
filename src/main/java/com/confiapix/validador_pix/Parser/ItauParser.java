package com.confiapix.validador_pix.Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.confiapix.validador_pix.Utils.DataHoraUtils;

public class ItauParser implements ComprovanteParser {

    @Override
    public boolean identificaBanco(String textoOCR) {
        return textoOCR.toLowerCase().contains("itaú") || textoOCR.toLowerCase().contains("itau");
    }

    @Override
    public Map<String, String> extrairCampos(String textoOCR) {
        Map<String, String> dados = new HashMap<>();

        dados.put("txId", extrairRegex(textoOCR, "(?i)ID\\s*da\\s*transa[cç][aã]o[:\\s]*([A-Z0-9]+)"));
        dados.put("nomePagador", extrairRegex(textoOCR, "(?i)De\\s*\\n([A-ZÀ-Ú\\s]+)(?=\\nCPF)"));
        dados.put("nomeRecebedor", extrairRegex(textoOCR, "(?i)Para\\s*\\n([A-ZÀ-Ú\\s]+)(?=\\nCPF)"));
        dados.put("valor", extrairRegex(textoOCR, "(?i)R\\$\\s*([\\d.,]+)"));

        // Regex único para data + hora
        String dataHoraBr = extrairRegex(
            textoOCR,
            "(?i)realizado\\s*em\\s*(\\d{2}/\\d{2}/\\d{4}\\s*[àas]*\\s*\\d{2}:\\d{2}:\\d{2})"
        );

        dados.put("dataHora", DataHoraUtils.converterParaISO(dataHoraBr));

        dados.put("banco", "Itaú");

        return dados;
    }


    private String extrairRegex(String texto, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

}