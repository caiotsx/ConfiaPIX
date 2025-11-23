package com.confiapix.validador_pix.Parser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.confiapix.validador_pix.Utils.DataHoraUtils;

public class BradescoParser implements ComprovanteParser {

    @Override
    public boolean identificaBanco(String textoOCR) {
        return textoOCR.toLowerCase().contains("bradesco");
    }

    @Override
    public Map<String, String> extrairCampos(String textoOCR) {
        Map<String, String> dados = new HashMap<>();

        dados.put("txId", extrairRegex(textoOCR, "(?i)Número de Controle\\s*([A-Z0-9]+)"));
        dados.put("nomePagador", extrairRegex(textoOCR, "(?i)Dados de quem fez a transação.*?Nome\\s*\\n([A-ZÀ-Ú\\s]+)(?=\\nCPF)"
));
        dados.put("nomeRecebedor", extrairRegex(textoOCR, "(?i)Dados de quem recebeu\\s*\\nNome\\s*\\n([^\\n]+)(?=\\\\n" + //
                        "CPF)\""));
        dados.put("valor", extrairRegex(textoOCR, "Valor[:\\s]*R?\\$?\\s*([\\d.,]+)"));

        String dataHoraStr = extrairRegex(textoOCR,
               "(\\d{2}/\\d{2}/\\d{4} - \\d{2}:\\d{2}:\\d{2})"
);
        LocalDateTime dataHora = DataHoraUtils.parseDataHora(dataHoraStr);
        dados.put("dataHora", dataHora != null ? dataHora.toString() : dataHoraStr);

        dados.put("banco", "Bradesco");
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