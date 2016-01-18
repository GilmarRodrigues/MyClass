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
import br.com.myclass.model.Aula;

/**
 * Created by gilmar on 21/12/15.
 */
public class AulaHelper {
    public EditText tConteudo;
    private EditText tDescricao;
    private EditText tData;
    private EditText tHoraInicial;
    private EditText tHoraFim;
    private Activity mContext;
    private Aula mAula;

    public AulaHelper(Activity context, View view) {
        tConteudo = (EditText) view.findViewById(R.id.edt_conteudo);
        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        tData = (EditText) view.findViewById(R.id.edt_data);
        tHoraInicial = (EditText) view.findViewById(R.id.edt_hora_inicio);
        tHoraFim = (EditText) view.findViewById(R.id.edt_hora_fim);
        mAula = new Aula();
        this.mContext = context;
    }

    public Aula pegaAuladoFormulario(String novoConteudo) {
        mAula.setConteudo(novoConteudo);
        mAula.setDescricao(tDescricao.getText().toString());
        return mAula;
    }

    public void colocaAulaNoFormulario(Aula aula) {
        mAula = aula;
        tConteudo.setText(aula.getConteudo());
        tDescricao.setText(aula.getDescricao());
        tData.setText(aula.getData());
        tHoraInicial.setText(aula.getHoraInicio());
        tHoraFim.setText(aula.getHoraFim());
    }

    public void offCampos() {
        tConteudo.setEnabled(false);
        tConteudo.setTextColor(mContext.getResources().getColor(R.color.black));
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
        tData.setEnabled(false);
        tData.setTextColor(mContext.getResources().getColor(R.color.black));
        tHoraInicial.setEnabled(false);
        tHoraInicial.setTextColor(mContext.getResources().getColor(R.color.black));
        tHoraFim.setEnabled(false);
        tHoraFim.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    public boolean ordenarLista() {
        List<String> auxList = new ArrayList<>();
        auxList.add(tHoraInicial.getText().toString());
        auxList.add(tHoraFim.getText().toString());

        Collections.sort(auxList, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("HH:mm");

            @Override
            public int compare(String h1, String h2) {
                try {
                    return f.parse(h1).compareTo(f.parse(h2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        if (tHoraInicial.getText().toString().equals(auxList.get(0))) {
            return false;
        } else {
            return true;
        }

    }

    public void alert(FragmentManager fragmentManager) {
        setError();
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Você deve preencher os campos Conteúdo e Data da aula.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    public void alertHora(FragmentManager fragmentManager) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Campo Hora de início da aula tem que ser menor que Hora encerramento da aula.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    private void setError(){
        tConteudo.setError("Informe o conteúdo");
        tData.setError("Informe a data da aula");
    }
}
