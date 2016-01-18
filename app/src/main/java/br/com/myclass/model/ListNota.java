package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 30/12/15.
 */
public class ListNota implements Serializable {
    public static final String KEY = "notas";
    public List<Nota> mNotas;

    public ListNota(List<Nota> mNotas) {
        this.mNotas = mNotas;
    }
}
