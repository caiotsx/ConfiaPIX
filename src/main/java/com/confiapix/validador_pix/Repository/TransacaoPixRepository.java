package com.confiapix.validador_pix.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.confiapix.validador_pix.Model.TransacaoPix;

@Repository
public interface TransacaoPixRepository extends JpaRepository<TransacaoPix, String> {

    Optional<TransacaoPix> findByTxId(String txId);

    List<TransacaoPix> findByPagadorIgnoreCase(String pagador);

    List<TransacaoPix> findByRecebedorIgnoreCase(String recebedor);

    @Query("SELECT t FROM TransacaoPix t WHERE LOWER(t.pagador) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<TransacaoPix> buscarPagadorAproximado(String nome);

    List<TransacaoPix> findByDatahoraBetween(LocalDateTime inicio, LocalDateTime fim);
}
