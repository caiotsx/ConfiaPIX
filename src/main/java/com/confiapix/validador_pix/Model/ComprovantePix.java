package com.confiapix.validador_pix.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Comprovante")
public class ComprovantePix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComprovante", nullable = true)
    private Long idComprovante;

    @Column(name = "txId", nullable = true)
    private String txId;

    @Column(name = "nomePagador", nullable = true)
    private String nomePagador;

    @Column(name = "nomeRecebedor", nullable = true)
    private String nomeRecebedor;

    @Column(name = "valor", nullable = true)
    private BigDecimal valor;

    @Column(name = "dataHora", nullable = true)
    private LocalDateTime dataHora;

    @Column(name = "nomeArquivo", nullable = true)
    private String nomeArquivo;

    @Column(name = "tipoArquivo", nullable = true)
    private String tipoArquivo;

    @Column(name = "dataEnvio", nullable = true)
    private LocalDateTime dataEnvio;

    @Column(name = "resultadoValidacao", nullable = true)
    private String resultadoValidacao;

    // Construtores
    public ComprovantePix() {
    }

    public ComprovantePix(Long idComprovante, String txId, String nomePagador, String nomeRecebedor, BigDecimal valor,
            LocalDateTime dataHora, String nomeArquivo, String tipoArquivo,
        LocalDateTime dataEnvio, String resultadoValidacao) {
        this.idComprovante = idComprovante;
        this.txId = txId;
        this.nomePagador = nomePagador;
        this.nomeRecebedor = nomeRecebedor;
        this.valor = valor;
        this.dataHora = dataHora;
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo;
        this.dataEnvio = dataEnvio;
        this.resultadoValidacao = resultadoValidacao;
    }

    // Getters & Setters
    public Long getIdComprovante() {
        return idComprovante;
    }

    public void setIdComprovante(Long idComprovante) {
        this.idComprovante = idComprovante;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getNomePagador() {
        return nomePagador;
    }

    public void setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
    }

    public String getNomeRecebedor() {
        return nomeRecebedor;
    }

    public void setNomeRecebedor(String nomeRecebedor) {
        this.nomeRecebedor = nomeRecebedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }


    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getResultadoValidacao() {
        return resultadoValidacao;
    }

    public void setResultadoValidacao(String resultadoValidacao) {
        this.resultadoValidacao = resultadoValidacao;
    }

}
