package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 02/01/16.
 */
public class Horario implements Serializable {
    public static final String KEY = "horario";
    public Horario horario;
    private Long id;
    private String dia;
    private String horaInicio;
    private String horaFim;
    private String descricao;
    private String status;
    private Long turmaId;

    public Horario() {
    }

    public Horario(Long id, String dia, String horaInicio, String horaFim, String descricao, String status, Long turmaId) {
        this.id = id;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
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

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
