package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 31/07/15.
 */
public class Turma implements Serializable {
    public static final String KEY = "turma";
    private Long id;
    private String disciplina;
    private String curso;
    private String dataInicial;
    private String dataFinal;
    private String descricao;
    private String status;

    public Turma() {
    }

    public Turma(Long id, String disciplina, String curso, String dataInicial, String dataFinal, String descricao, String status) {
        this.id = id;
        this.disciplina = disciplina;
        this.curso = curso;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.descricao = descricao;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }
}
