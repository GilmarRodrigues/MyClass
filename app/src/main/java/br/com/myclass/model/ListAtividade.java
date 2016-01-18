package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 22/12/15.
 */
public class ListAtividade implements Serializable {
    public static final String KEY = "atividades";
    public List<Atividade> mAtividades;

    public ListAtividade(List<Atividade> mAtividades) {
        this.mAtividades = mAtividades;
    }
}
