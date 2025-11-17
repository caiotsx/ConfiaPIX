package com.confiapix.validador_pix.Utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataHoraUtils {

    // Mapa fixo para converter meses abreviados PT-BR
    private static final Map<String, String> MESES = new LinkedHashMap<>();
    static {
        MESES.put("JAN", "01");
        MESES.put("FEV", "02");
        MESES.put("MAR", "03");
        MESES.put("ABR", "04");
        MESES.put("MAI", "05");
        MESES.put("JUN", "06");
        MESES.put("JUL", "07");
        MESES.put("AGO", "08");
        MESES.put("SET", "09");
        MESES.put("OUT", "10");
        MESES.put("NOV", "11");
        MESES.put("DEZ", "12");
    }

    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),

            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,

            DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd MM yyyy HH:mm")
    };

    public static String converterParaISO(String dataHoraBr) {
        if (dataHoraBr == null)
            return null;

        // Remove "às", "as", variações
        String normalizado = dataHoraBr.replaceAll("(?i)às|as|às", "").trim();

        // Normaliza múltiplos espaços para apenas um
        normalizado = normalizado.replaceAll("\\s+", " ");

        // Agora o texto sempre terá um único espaço entre data e hora
        DateTimeFormatter formatoBr = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        LocalDateTime dt = LocalDateTime.parse(normalizado, formatoBr);

        return dt.toString(); // ISO 8601
    }

    public static LocalDateTime parseDataHora(String raw) {

        if (raw == null || raw.isBlank())
            return null;

        String t = raw.trim().toUpperCase();

        // 🔥 Se for ISO, não mexe! Retorna na hora
        if (t.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")) {
            try {
                return OffsetDateTime.parse(t).toLocalDateTime();
            } catch (Exception e1) {
                return LocalDateTime.parse(t); // fallback
            }
        }

        // 🔧 Agora sim pode normalizar para os formatos BR
        t = t.replace(" - ", " ")
                .replaceAll("\\s+", " ");

        // 🔥 Converte meses PT-BR (JAN, FEV, OUT etc.)
        for (var entry : MESES.entrySet()) {
            String m = entry.getKey();
            if (t.contains(" " + m + " ")) {
                t = t.replace(" " + m + " ", " " + entry.getValue() + " ");
            }
        }

        // Tenta formatadores
        for (DateTimeFormatter f : FORMATTERS) {
            try {
                return LocalDateTime.parse(t, f);
            } catch (Exception ignored) {
            }
        }

        throw new DateTimeException("Formato não reconhecido: " + raw);
    }
}
