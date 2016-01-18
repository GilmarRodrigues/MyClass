package br.com.myclass.helper;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;

import br.com.myclass.R;
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 05/01/16.
 */
public class ProvaHelper {
    private EditText tData;
    private EditText tHora;
    private EditText tDescricao;
    private Prova mProva;
    private Activity mContext;
    
    public ProvaHelper(Activity context, View view) {
        this.mContext = context;
        mProva = new Prova();
        tData = (EditText) view.findViewById(R.id.edt_data);
        tHora = (EditText) view.findViewById(R.id.edt_hora);
        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        
    }

    public Prova pegaProvaDoFormulario(String novaDescricao) {
        mProva.setDescricao(novaDescricao);
        return mProva;
    }

    public Prova colocaProvaNoFormulario(Prova prova) {
        mProva = prova;
        tData.setText(prova.getData());
        tHora.setText(prova.getHora());
        tDescricao.setText(prova.getDescricao());
        return mProva;

    }

    public void offCampos() {
        tData.setEnabled(false);
        tData.setTextColor(mContext.getResources().getColor(R.color.black));
        tHora.setEnabled(false);
        tHora.setTextColor(mContext.getResources().getColor(R.color.black));
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    public void alert(FragmentManager fragmentManager) {
        setError();
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Você deve preencher os campos Descrição e Data da prova.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    private void setError(){
        tDescricao.setError("Informe a descrição");
        tData.setError("Informe a data da prova");
    }

    public EditText gettDescricao() {
        return tDescricao;
    }
}
