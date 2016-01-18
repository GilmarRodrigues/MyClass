package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 21/12/15.
 */
public class ListAula implements Serializable {
    public static final String KEY = "aulas";
    public List<Aula> mAulas;

    public ListAula(List<Aula> mAulas) {
        this.mAulas = mAulas;
    }
}
