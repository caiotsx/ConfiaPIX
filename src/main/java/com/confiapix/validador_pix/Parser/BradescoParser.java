package com.confiapix.validador_pix.Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BradescoParser implements ComprovanteParser {

    @Override
    public boolean identificaBanco(String textoOCR) {
        return textoOCR.toLowerCase().contains("bradesco");
    }

    @Override
    public Map<String, String> extrairCampos(String textoOCR) {
        Map<String, String> dados = new HashMap<>();

        // Regex para capturar os campos principais
        Pattern valorPattern = Pattern.compile("Valor[:\\s]*R?\\$?\\s*([\\d.,]+)", Pattern.CASE_INSENSITIVE);
        Pattern dataHoraPattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s*-\\s*(\\d{2}:\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
        Pattern nomeRecebedorPattern = Pattern.compile("Dados de quem recebeu.*?Nome\\s*\\n([A-ZÀ-Ú\\s]+)(?=\\nCPF)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern nomePagadorPattern = Pattern.compile("Dados de quem fez a transação.*?Nome\\s*\\n([A-ZÀ-Ú\\s]+)(?=\\nCPF)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern txIdPattern = Pattern.compile("Número de Controle\\s*([A-Z0-9]+)", Pattern.CASE_INSENSITIVE);

        Matcher mValor = valorPattern.matcher(textoOCR);
        Matcher mDataHora = dataHoraPattern.matcher(textoOCR);
        Matcher mNomeRecebedor = nomeRecebedorPattern.matcher(textoOCR);
        Matcher mNomePagador = nomePagadorPattern.matcher(textoOCR);
        Matcher mTxId = txIdPattern.matcher(textoOCR);

        if (mValor.find()) dados.put("valor", mValor.group(1).trim());
        if (mDataHora.find()) dados.put("dataHora", mDataHora.group(1) + " " + mDataHora.group(2));
        if (mNomeRecebedor.find()) dados.put("nomeRecebedor", mNomeRecebedor.group(1).trim());
        if (mNomePagador.find()) dados.put("nomePagador", mNomePagador.group(1).trim());
        if (mTxId.find()) dados.put("txId", mTxId.group(1).trim());

        dados.put("banco", "Bradesco");
        return dados;
    }
}