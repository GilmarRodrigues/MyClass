package br.com.myclass.helper;

import android.app.Activity;
import android.view.View;

import com.rey.material.widget.EditText;

import br.com.myclass.R;
import br.com.myclass.model.Nota;

/**
 * Created by gilmar on 30/12/15.
 */
public class NotaHelper extends Nota {
    private EditText tNota;
    private EditText tProntoExtra;
    private EditText tDescricao;
    private Activity mContext;
    private Nota mNota;

    public NotaHelper(Activity context, View view) {
        tNota = (EditText) view.findViewById(R.id.edt_nota);
        tProntoExtra = (EditText) view.findViewById(R.id.edt_ponto_extra);
        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        this.mContext = context;
        mNota = new Nota();
    }

    public Nota pegaNotaDoFormulario() {
        mNota.setNota(tNota.getText().toString());
        mNota.setPontoExtra(tProntoExtra.getText().toString());
        mNota.setDescricao(tDescricao.getText().toString());
        return mNota;
    }

    public void colocaNotaNoFormulario(Nota nota) {
        mNota = nota;
        tNota.setText(nota.getNota());
        tProntoExtra.setText(nota.getPontoExtra());
        tDescricao.setText(nota.getDescricao());
    }
}
