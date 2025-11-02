package com.confiapix.validador_pix.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.confiapix.validador_pix.Model.ComprovantePix;
import com.confiapix.validador_pix.Repository.ComprovantePixRepository;

@Service
public class ComprovantePixService {

    @Autowired
    private ComprovantePixRepository comprovantePixRepository;

    // Criar ou salvar comprovante
    public ComprovantePix save(ComprovantePix comprovantePix) {
        return comprovantePixRepository.save(comprovantePix);
    }

    // Listar todos
    public List<ComprovantePix> findAll() {
        return comprovantePixRepository.findAll();
    }

    // Buscar por ID
    public Optional<ComprovantePix> findById(Long id) {
        return comprovantePixRepository.findById(id);
    }

    // Buscar por TXID
    public Optional<ComprovantePix> findByTxId(String txId) {
        return comprovantePixRepository.findByTxId(txId);
    }

    // Atualizar comprovante
    public Optional<ComprovantePix> update(Long id, ComprovantePix dadosAtualizados) {
        return comprovantePixRepository.findById(id).map(existente -> {
            existente.setValor(dadosAtualizados.getValor());
            existente.setResultadoValidacao(dadosAtualizados.getResultadoValidacao());
            existente.setDataHora(dadosAtualizados.getDataHora());
            existente.setNomePagador(dadosAtualizados.getNomePagador());
            existente.setNomeRecebedor(dadosAtualizados.getNomeRecebedor());
            return comprovantePixRepository.save(existente);
        });
    }

    // Deletar comprovante por ID
    public boolean deleteById(Long id) {
        if (comprovantePixRepository.existsById(id)) {
            comprovantePixRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validação fictícia — cruzamento de dados (simulação futura com Open Finance)
    public String validarComprovante(ComprovantePix comprovante) {
        // Aqui futuramente entrará o OCR + verificação com Open Finance
        if (comprovante.getValor() != null && comprovante.getValor().compareTo(new java.math.BigDecimal("0")) > 0) {
            comprovante.setResultadoValidacao("Comprovante válido e confirmado.");
        } else {
            comprovante.setResultadoValidacao("Comprovante inválido ou suspeito.");
        }

        comprovantePixRepository.save(comprovante);
        return comprovante.getResultadoValidacao();
    }
}