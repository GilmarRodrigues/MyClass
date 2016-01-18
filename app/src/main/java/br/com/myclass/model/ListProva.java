package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 28/12/15.
 */
public class ListProva implements Serializable {
    public static final String KEY = "provas";
    public List<Prova> mProvas;

    public ListProva(List<Prova> mProvas) {
        this.mProvas = mProvas;
    }
}
