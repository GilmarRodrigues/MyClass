package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 09/08/15.
 */
public class Comportamento implements Serializable {
    private Long id;
    private String comportamento;
    private String data;
    private String comentario;
    private Long alunoId;

    public Comportamento() {
    }

    public Comportamento(Long id, String comportamento, String data, String comentario, Long alunoId) {
        this.id = id;
        this.comportamento = comportamento;
        this.data = data;
        this.comentario = comentario;
        this.alunoId = alunoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComportamento() {
        return comportamento;
    }

    public void setComportamento(String comportamento) {
        this.comportamento = comportamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

