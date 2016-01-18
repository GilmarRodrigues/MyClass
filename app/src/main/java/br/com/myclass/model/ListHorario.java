package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 02/01/16.
 */
public class ListHorario implements Serializable {
    public static final String KEY = "horarios";
    public List<Horario> mHorarios;

    public ListHorario(List<Horario> mHorarios) {
        this.mHorarios = mHorarios;
    }
}
