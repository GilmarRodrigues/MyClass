package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 01/08/15.
 */
public class ListAluno implements Serializable{
    public static final String KEY = "alunos";
    public List<Aluno> mAlunos;

    public ListAluno(List<Aluno> mAlunos) {
        this.mAlunos = mAlunos;
    }
}
