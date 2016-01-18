package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 31/07/15.
 */
public class ListTurma implements Serializable {
    public static final String KEY = "turmas";
    public List<Turma> mTurmas;

    public ListTurma(List<Turma> mTurmas) {
        this.mTurmas = mTurmas;
    }
}
