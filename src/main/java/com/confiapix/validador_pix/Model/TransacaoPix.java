package com.confiapix.validador_pix.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transacao_pix")
public class TransacaoPix {

    @Id
    @Column(name = "txid", nullable = true)
    private String txId;

    @Column(name = "pagador", nullable = true)
    private String pagador;

    @Column(name = "recebedor", nullable = true)
    private String recebedor;

    @Column(name = "valor", nullable = true)
    private BigDecimal valor;

    @Column(name = "datahora", nullable = true)
    private LocalDateTime datahora;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public String getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(String recebedor) {
        this.recebedor = recebedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDatahora() {
        return datahora;
    }

    public void setDatahora(LocalDateTime datahora) {
        this.datahora = datahora;
    }

    public TransacaoPix(String txId, String pagador, String recebedor, BigDecimal valor, LocalDateTime datahora) {
        this.txId = txId;
        this.pagador = pagador;
        this.recebedor = recebedor;
        this.valor = valor;
        this.datahora = datahora;
    }

    public TransacaoPix() {
    }

}
