package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 28/12/15.
 */
public class Prova implements Serializable {
    private static  final String KEY = "prova";
    private Long id;
    private String data;
    private String hora;
    private String descricao;
    private String status;
    private Long turmaId;

    public Prova() { }

    public Prova(Long id, String data, String hora, String descricao, String status, Long turmaId) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
        this.status = status;
        this.turmaId = turmaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }
}
