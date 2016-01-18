package br.com.myclass.model;

import java.io.Serializable;

/**
 * Created by gilmar on 28/12/15.
 */
public class AulaProva implements Serializable {
    private static final String KEY = "aulaprova";
    private Long id;
    private Long aulaId;
    private Long provaId;
    private String status;

    public AulaProva() {
    }

    public AulaProva(Long id, Long aulaId, Long provaId, String status) {
        this.id = id;
        this.aulaId = aulaId;
        this.provaId = provaId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getProvaId() {
        return provaId;
    }

    public void setProvaId(Long provaId) {
        this.provaId = provaId;
    }
}
