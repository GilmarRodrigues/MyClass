package br.com.myclass.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.helper.AtividadeHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAula;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 26/12/15.
 */
public class AdicionarAtividadeDialog extends BaseDialog {
    private Callback callback;
    private Aula mAula;
    private Turma mTurma;
    private Atividade mAtividade;
    private AtividadeHelper mHelper;
    private TextView tAddAlunoAtividade;
    private LinearLayout lnAlunoAtividade;
    private List<Aluno> mListAlunos;

    public static void show(FragmentManager fm, Aula aluno, Turma turma, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("adicionar_atividade");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        AdicionarAtividadeDialog frag = new AdicionarAtividadeDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListAula.KEY, aluno);
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "adicionar_atividade");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Nova Atividade");
        View view = inflater.inflate(R.layout.dialog_atividade, container, false);
        mHelper = new AtividadeHelper(getActivity(), view);
        mAtividade = new Atividade();
        this.mAula = (Aula) getArguments().getSerializable(ListAula.KEY);
        this.mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setText("Salvar");
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        lnAlunoAtividade = (LinearLayout) view.findViewById(R.id.ln_aluno_atividade);
        tAddAlunoAtividade = (TextView) view.findViewById(R.id.tv_aluno_atividade);
        tAddAlunoAtividade.setOnClickListener(onClickAddAlunoAtividade());

        return view;
    }

    private View.OnClickListener onClickAddAlunoAtividade() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAddEdAlunoAtividadeDialog.show(getFragmentManager(), mTurma, mListAlunos, new ListAddEdAlunoAtividadeDialog.Callback() {

                    @Override
                    public void onAddAlunoAtividade(List<Aluno> alunoList) {
                        mListAlunos = alunoList;
                        lnAlunoAtividade.removeAllViews();
                        for (int i = 0; i < alunoList.size(); i++) {
                            if (alunoList.get(i).getStatus().equals("ativo")) {
                                TextView tv = new TextView(getActivity());
                                tv.setText(alunoList.get(i).getNome() + " " + alunoList.get(i).getSobrenome());
                                lnAlunoAtividade.addView(tv);
                            }
                        }
                    }
                });
            }
        };
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                mAtividade = mHelper.pegaAtividadeDoFormulario();
                if (mAtividade == null) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                mAtividade.setStatus("ativo");
                mAtividade.setAulaId(mAula.getId());
                AtividadeDAO dao = new AtividadeDAO(context);
                long id = dao.inserir(mAtividade);
                dao.close();

                if (mListAlunos != null) {
                    AlunoAtividadeDAO alunoAtivDao = new AlunoAtividadeDAO(context);
                    for (Aluno aluno : mListAlunos) {
                        if (aluno.getStatus().equals("ativo")) {
                            AlunoAtividade alunoAtividade = new AlunoAtividade();
                            alunoAtividade.setAlunoId(aluno.getId());
                            alunoAtividade.setAtividadeId(id);
                            alunoAtividade.setStatus("feito");
                            alunoAtivDao.inserir(alunoAtividade);
                        }
                    }
                    alunoAtivDao.close();
                }

                if (callback != null) {
                    callback.onAtividadeAdd(mAtividade);
                }
                dismiss();
            }
        };
    }

    private View.OnClickListener onClickCancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    public static interface Callback {
        public void onAtividadeAdd(Atividade atividade);
    }
}
