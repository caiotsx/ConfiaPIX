package com.confiapix.validador_pix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.confiapix.validador_pix.Model.ComprovantePix;

import java.util.Optional;

@Repository
public interface ComprovantePixRepository extends JpaRepository<ComprovantePix, Long> {

    // Buscar um comprovante pelo TXID (único no PIX)
    Optional<ComprovantePix> findByTxId(String txId);

    // Verificar se um comprovante já foi cadastrado
    boolean existsByTxId(String txId);
}