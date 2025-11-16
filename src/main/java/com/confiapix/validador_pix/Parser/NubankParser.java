package com.confiapix.validador_pix.Parser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.confiapix.validador_pix.Utils.DataHoraUtils;

public class NubankParser implements ComprovanteParser {

    @Override
    public boolean identificaBanco(String textoOCR) {
        return textoOCR.toLowerCase().contains("nubank");
    }

    @Override
    public Map<String, String> extrairCampos(String textoOCR) {
        Map<String, String> dados = new HashMap<>();

        dados.put("txId", extrairRegex(textoOCR, "(?i)ID da transação[:\\s]*([A-Za-z0-9-]+)"));
        dados.put("nomePagador", extrairRegex(textoOCR, "(?i)Origem\\n" + //
                "Nome[:\\s]*(.+)"));
        dados.put("nomeRecebedor", extrairRegex(textoOCR, "(?i)Destino\\n" + //
                "Nome[:\\s]*(.+)"));
        dados.put("valor", extrairRegex(textoOCR, "(?i)valor[:\\s]*R?\\$?\\s*([\\d.,]+)"));

        String dataHoraStr = extrairRegex(textoOCR,
                "(?i)(\\d{1,2}\\s*[A-Z]{3}\\s*\\d{4}\\s*-\\s*\\d{2}:\\d{2}:\\d{2})");
        LocalDateTime dataHora = DataHoraUtils.parseDataHora(dataHoraStr);
        dados.put("dataHora", dataHora != null ? dataHora.toString() : dataHoraStr);

        dados.put("banco", "Nubank");
        return dados;
    }

    private String extrairRegex(String texto, String padrao) {
        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}