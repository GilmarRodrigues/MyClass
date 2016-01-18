package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 09/08/15.
 */
public class ListComportamento implements Serializable {
    public static final String KEY = "comportamentos";
    public List<Comportamento> mComportamentos;

    public ListComportamento(List<Comportamento> mComportamentos) {
        this.mComportamentos = mComportamentos;
    }
}
