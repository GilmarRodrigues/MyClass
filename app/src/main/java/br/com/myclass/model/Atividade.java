package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 22/12/15.
 */
public class Atividade implements Serializable {
    public static final String KEY = "atividade";
    private Long id;
    private String tipo;
    private String descricao;
    private String status;
    private Long aulaId;

    public Atividade() {
    }

    public Atividade(Long id, String tipo, String descricao, String status, Long aulaId) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.status = status;
        this.aulaId = aulaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAulaId() {
        return aulaId;
    }

    public void setAulaId(Long aulaId) {
        this.aulaId = aulaId;
    }
}
