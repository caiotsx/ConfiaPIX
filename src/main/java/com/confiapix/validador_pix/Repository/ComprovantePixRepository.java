package com.confiapix.validador_pix.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.confiapix.validador_pix.Model.ComprovantePix;

@Repository
public interface ComprovantePixRepository extends JpaRepository<ComprovantePix, Long> {

    Optional<ComprovantePix> findByTxId(String txId);

    boolean existsByTxId(String txId);
}