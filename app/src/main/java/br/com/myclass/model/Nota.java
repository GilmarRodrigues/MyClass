package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 30/12/15.
 */
public class Nota implements Serializable {
    public static final String KEY = "nota";
    private Long id;
    private String nota;
    private String pontoExtra;
    private String descricao;
    private String status;
    private Long alunoId;
    private Long provaId;

    public Nota() {
    }

    public Nota(Long id, String nota, String pontoExtra, String descricao, String status, Long alunoId, Long provaId) {
        this.id = id;
        this.nota = nota;
        this.pontoExtra = pontoExtra;
        this.descricao = descricao;
        this.status = status;
        this.alunoId = alunoId;
        this.provaId = provaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getPontoExtra() {
        return pontoExtra;
    }

    public void setPontoExtra(String pontoExtra) {
        this.pontoExtra = pontoExtra;
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

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getProvaId() {
        return provaId;
    }

    public void setProvaId(Long provaId) {
        this.provaId = provaId;
    }
}
