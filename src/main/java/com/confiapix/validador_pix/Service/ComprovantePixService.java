package com.confiapix.validador_pix.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.confiapix.validador_pix.Model.ComprovantePix;
import com.confiapix.validador_pix.Model.TransacaoPix;
import com.confiapix.validador_pix.Repository.ComprovantePixRepository;
import com.confiapix.validador_pix.Repository.TransacaoPixRepository;
import com.confiapix.validador_pix.Utils.DataHoraUtils;

@Service
public class ComprovantePixService {

    private final ComprovantePixRepository comprovantePixRepository;
    private final TransacaoPixRepository transacaoPixRepository;

    public ComprovantePixService(
            ComprovantePixRepository comprovantePixRepository,
            TransacaoPixRepository transacaoPixRepository) {

        this.comprovantePixRepository = comprovantePixRepository;
        this.transacaoPixRepository = transacaoPixRepository;
    }

    private BigDecimal normalizarValor(String valorStr) {
        if (valorStr == null)
            return null;

        valorStr = valorStr.replace(".", "").replace(",", ".");

        return new BigDecimal(valorStr);
    }

    public ComprovantePix save(ComprovantePix comprovantePix) {
        comprovantePix.setDataEnvio(LocalDateTime.now());
        return comprovantePixRepository.save(comprovantePix);
    }

    public List<ComprovantePix> findAll() {
        return comprovantePixRepository.findAll();
    }

    public Optional<ComprovantePix> findById(Long id) {
        return comprovantePixRepository.findById(id);
    }

    public Optional<ComprovantePix> findByTxId(String txId) {
        return comprovantePixRepository.findByTxId(txId);
    }

    public Optional<ComprovantePix> update(Long id, ComprovantePix dadosAtualizados) {
        return comprovantePixRepository.findById(id).map(existente -> {
            existente.setValor(dadosAtualizados.getValor());
            existente.setDataHora(dadosAtualizados.getDataHora());
            existente.setNomePagador(dadosAtualizados.getNomePagador());
            existente.setNomeRecebedor(dadosAtualizados.getNomeRecebedor());
            return comprovantePixRepository.save(existente);
        });
    }

    public boolean deleteById(Long id) {
        if (comprovantePixRepository.existsById(id)) {
            comprovantePixRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String, Object> validar(Map<String, String> dadosOcr) {

        String txid = dadosOcr.get("txId");

        Optional<TransacaoPix> transacaoOpt = transacaoPixRepository.findByTxId(txid);

        if (transacaoOpt.isEmpty()) {
            return Map.of(
                    "valido", false,
                    "motivo", "Transação não encontrada na base oficial.");
        }

        TransacaoPix oficial = transacaoOpt.get();

        boolean pagadorOK = comparar(oficial.getPagador(), dadosOcr.get("nomePagador"));
        boolean recebedorOK = comparar(oficial.getRecebedor(), dadosOcr.get("nomeRecebedor"));
        BigDecimal valorOcr = normalizarValor(dadosOcr.get("valor"));
        boolean valorOK = oficial.getValor().compareTo(valorOcr) == 0;

        boolean dataOK = oficial.getDatahora().equals(DataHoraUtils.parseDataHora(dadosOcr.get("dataHora")));

        boolean tudoOK = pagadorOK && recebedorOK && valorOK && dataOK;

        
        if (pagadorOK == false) {
            return Map.of(
                    "valido", false,
                    "motivo", "Nome do Pagador não condiz com a Transação Original.");
        }

        if (recebedorOK == false) {
            return Map.of(
                    "valido", false,
                    "motivo", "Nome do Recebedor não condiz com a Transação Original.");
        }

        if (valorOK == false) {
            return Map.of(
                    "valido", false,
                    "motivo", "Valor não condiz com a Transação Original.");
        }

        if (dataOK == false) {
            return Map.of(
                    "valido", false,
                    "motivo", "Data não condiz com a Transação Original.");
        }

        return Map.of(
                "valido", tudoOK,
                "campos", Map.of(
                        "nomePagador", pagadorOK,
                        "nomeRecebedor", recebedorOK,
                        "valor", valorOK,
                        "dataHora", dataOK));
    }


    private boolean comparar(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

}