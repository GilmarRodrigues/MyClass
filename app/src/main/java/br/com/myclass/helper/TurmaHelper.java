package br.com.myclass.helper;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 20/12/15.
 */
public class TurmaHelper {
    public EditText tDisciplina;
    public EditText tCurso;
    private EditText tDataInicial;
    private EditText tDataFinal;
    private EditText tDescricao;
    private Activity mContext;
    private Turma mTurma;

    public TurmaHelper(Activity context, View view) {
        tDisciplina = (EditText) view.findViewById(R.id.edt_disciplina);
        tCurso = (EditText) view.findViewById(R.id.edt_curso);
        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        tDataInicial = (EditText) view.findViewById(R.id.edt_data_inicial);
        tDataFinal = (EditText) view.findViewById(R.id.edt_data_fim);
        this.mContext = context;
        mTurma = new Turma();
    }

    public Turma pegaTurmaDoFormulario(String novaDisciplina, String novoCurso) {
        mTurma.setDisciplina(novaDisciplina);
        mTurma.setCurso(novoCurso);
        mTurma.setDescricao(tDescricao.getText().toString());
        return mTurma;
    }

    public void colocaTurmaNoFormulario(Turma turma){
        mTurma = turma;
        tDisciplina.setText(turma.getDisciplina());
        tCurso.setText(turma.getCurso());
        tDescricao.setText(turma.getDescricao());
        tDataInicial.setText(turma.getDataInicial());
        tDataFinal.setText(turma.getDataFinal());
    }

    public void offCampos(){
        tDisciplina.setEnabled(false);
        tDisciplina.setTextColor(mContext.getResources().getColor(R.color.black));
        tCurso.setEnabled(false);
        tCurso.setTextColor(mContext.getResources().getColor(R.color.black));
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
        tDataInicial.setEnabled(false);
        tDataInicial.setTextColor(mContext.getResources().getColor(R.color.black));
        tDataFinal.setEnabled(false);
        tDataFinal.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    public boolean ordenarLista() {
        List<String> auxList = new ArrayList<>();
        auxList.add(tDataInicial.getText().toString());
        auxList.add(tDataFinal.getText().toString());

        Collections.sort(auxList, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(String h1, String h2) {
                try {
                    return f.parse(h1).compareTo(f.parse(h2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        if (tDataInicial.getText().toString().equals(auxList.get(0))) {
            return false;
        } else {
            return true;
        }

    }

    public void alert(FragmentManager fragmentManager) {
        setError();
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Você deve preencher os campos Disciplina, Curso/Série, Data de início e Data de encerramento.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    public void alertHora(FragmentManager fragmentManager) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Campo Data de início tem que ser menor que Data de encerramento.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    private void setError(){
        tDisciplina.setError("Informe a Disciplina");
        tCurso.setError("Informe o Curso/Série");
        tDataInicial.setError("Informe a Data de início");
        tDataFinal.setError("Informe a Data de encerramento");
    }
}
