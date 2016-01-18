package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 27/12/15.
 */
public class AlunoAtividade implements Serializable{
    private static final String KEY = "alunoatividade";
    private Long id;
    private Long alunoId;
    private Long atividadeId;
    private String status;

    public AlunoAtividade() {
    }

    public AlunoAtividade(Long id, Long alunoId, Long atividadeId, String status) {
        this.id = id;
        this.alunoId = alunoId;
        this.atividadeId = atividadeId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getAtividadeId() {
        return atividadeId;
    }

    public void setAtividadeId(Long atividadeId) {
        this.atividadeId = atividadeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
