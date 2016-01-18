package br.com.myclass.helper;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;

import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;

import br.com.myclass.R;
import br.com.myclass.model.Atividade;

/**
 * Created by gilmar on 26/12/15.
 */
public class AtividadeHelper {
    public Spinner spnTipo;
    private EditText tDescricao;
    public Atividade mAtividade;
    private Activity mContext;
    private String[] items;
    private String novoTipo = null;

    public AtividadeHelper() {
    }

    public AtividadeHelper(Activity context, View view) {
        spnTipo = (Spinner) view.findViewById(R.id.spinner_tipo);
        todasAtividades();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spnTipo.setAdapter(adapter);
        spnTipo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (position > 0) {
                    novoTipo = items[position].toString();
                } else {
                    novoTipo = null;
                }
            }
        });

        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        mAtividade = new Atividade();
        this.mContext = context;
    }

    public Atividade pegaAtividadeDoFormulario() {
        if (novoTipo != null) {
            mAtividade.setTipo(novoTipo);
        } else {
            return null;
        }
        mAtividade.setDescricao(tDescricao.getText().toString());
        return mAtividade;
    }

    public Atividade colocaAtividadeNoFormulario(Atividade atividade) {
        mAtividade = atividade;
        spnTipo.setSelection(getTipo(atividade));
        tDescricao.setText(atividade.getDescricao());
        return mAtividade;
    }

    private int getTipo(Atividade atividade) {
        for (int i = 0; i < items.length; i++){
            if (atividade.getTipo().equals(items[i])){
                return i;
            }
        }
        return 0;
    }

    public void offCampos() {
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
        spnTipo.setEnabled(false);
    }

    private void todasAtividades() {
        items = new String[11];
        items[0] = "Selecione uma item";
        items[1] = "Chamada";
        items[2] = "Comportamento";
        items[3] = "Exercício";
        items[4] = "Feira de ciência ou cultural";
        items[5] = "Leitura";
        items[6] = "Passeio/Viagem";
        items[7] = "Participação";
        items[8] = "Seminário";
        items[9] = "Trabalho";
        items[10] = "Outras";
    }

    public String[] atividades() {
        String[] items = new String[10];
        items[0] = "Chamada";
        items[1] = "Comportamento";
        items[2] = "Exercício";
        items[3] = "Feira de ciência ou cultural";
        items[4] = "Leitura";
        items[5] = "Passeio/Viagem";
        items[6] = "Participação";
        items[7] = "Seminário";
        items[8] = "Trabalho";
        items[9] = "Outras";
        return items;
    }

    public void alert(FragmentManager fragmentManager) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Você deve selecionar um Tipo de Atividade.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

}
