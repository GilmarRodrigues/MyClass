package br.com.myclass.helper;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;

import br.com.myclass.R;
import br.com.myclass.model.Aluno;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 01/08/15.
 */
public class AlunoHelper {
    public EditText tNome;
    public EditText tSobrenome;
    private EditText tEndereco;
    private EditText tTelefone;
    private EditText tEmail;
    private EditText tDescricao;
    private EditText tNomeResp;
    private EditText tTelefoneResp;
    private EditText tEmailResp;
    private RoundedImageView ivFoto;
    private EditText tDataNascimento;
    public Aluno mAluno;
    private Activity mContext;

    public AlunoHelper(Activity context, View view) {
        tNome = (EditText) view.findViewById(R.id.edt_nome);
        tSobrenome = (EditText) view.findViewById(R.id.edt_sobrenome);
        tEndereco = (EditText) view.findViewById(R.id.edt_endereco);
        tTelefone = (EditText) view.findViewById(R.id.edt_telefone);
        tEmail = (EditText) view.findViewById(R.id.edt_email);
        tDescricao = (EditText) view.findViewById(R.id.edt_descricao);
        tNomeResp = (EditText) view.findViewById(R.id.edt_nome_responsavel);
        tTelefoneResp = (EditText) view.findViewById(R.id.edt_telefone_responsavel);
        tEmailResp = (EditText) view.findViewById(R.id.edt_email_responsavel);
        tDataNascimento = (EditText) view.findViewById(R.id.edt_data_nascimento);
        this.mContext = context;
        mAluno = new Aluno();
    }

    public Aluno pegaProdutoDoFormulario(String novaNome, String novoSobrenome) {
        mAluno.setNome(novaNome);
        mAluno.setSobrenome(novoSobrenome);
        mAluno.setEndereco(tEndereco.getText().toString());
        mAluno.setTelefone(tTelefone.getText().toString());
        mAluno.setEmail(tEmail.getText().toString());
        mAluno.setDescricao(tDescricao.getText().toString());
        mAluno.setNomeResponsavel(tNomeResp.getText().toString());
        mAluno.setTelefoneResponsavel(tTelefoneResp.getText().toString());
        mAluno.setEmailResponsavel(tEmailResp.getText().toString());
        return mAluno;
    }

    public void colocaAlunoNoFormulario(Aluno aluno) {
        mAluno = aluno;
        tNome.setText(aluno.getNome());
        tSobrenome.setText(aluno.getSobrenome());
        tEndereco.setText(aluno.getEndereco());
        tTelefone.setText(aluno.getTelefone());
        tEmail.setText(aluno.getEmail());
        tDescricao.setText(aluno.getDescricao());
        tNomeResp.setText(aluno.getNomeResponsavel());
        tTelefoneResp.setText(aluno.getTelefoneResponsavel());
        tEmailResp.setText(aluno.getEmailResponsavel());
        tDataNascimento.setText(aluno.getDataNascimento());
    }

    public void offCampos() {
        tNome.setEnabled(false);
        tNome.setTextColor(mContext.getResources().getColor(R.color.black));
        tSobrenome.setEnabled(false);
        tSobrenome.setTextColor(mContext.getResources().getColor(R.color.black));
        tEndereco.setEnabled(false);
        tEndereco.setTextColor(mContext.getResources().getColor(R.color.black));
        tTelefone.setEnabled(false);
        tTelefone.setTextColor(mContext.getResources().getColor(R.color.black));
        tEmail.setEnabled(false);
        tEmail.setTextColor(mContext.getResources().getColor(R.color.black));
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
        tNomeResp.setEnabled(false);
        tNomeResp.setTextColor(mContext.getResources().getColor(R.color.black));
        tTelefoneResp.setEnabled(false);
        tTelefoneResp.setTextColor(mContext.getResources().getColor(R.color.black));
        tEmailResp.setEnabled(false);
        tEmailResp.setTextColor(mContext.getResources().getColor(R.color.black));
        tDataNascimento.setEnabled(false);
        tDataNascimento.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    public void alert(FragmentManager fragmentManager) {
        setError();
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Você deve preencher os campos Nome e Sobrenome.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    private void setError(){
        tNome.setError("Informe o nome");
        tSobrenome.setError("Informe o sobrenome");
    }
}
