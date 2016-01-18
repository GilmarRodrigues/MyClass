package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 27/12/15.
 */
public class ListAlunoAtividade implements Serializable {
    public static final String KEY = "alunoatividades";
    public List<AlunoAtividade> mAlunoAtividades;

    public ListAlunoAtividade(List<AlunoAtividade> mAlunoAtividades) {
        this.mAlunoAtividades = mAlunoAtividades;
    }
}
