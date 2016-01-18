package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 21/12/15.
 */
public class Aula implements Serializable {
    public static final String KEY = "aula";
    private Long id;
    private String conteudo;
    private String data;
    private String horaInicio;
    private String horaFim;
    private String descricao;
    private Long turmaId;
    private String status;

    public Aula() {
    }

    public Aula(Long id, String conteudo, String data, String horaInicio, String horaFim, String descricao, Long turmaId, String status) {
        this.id = id;
        this.conteudo = conteudo;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.descricao = descricao;
        this.turmaId = turmaId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
