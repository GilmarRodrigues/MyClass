package br.com.myclass.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.helper.AtividadeHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.ListAtividade;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 27/12/15.
 */
public class EditarAtividadeDialog extends BaseDialog {
    private Callback callback;
    private AtividadeHelper mHelper;
    private Atividade mAtividade;
    private Turma mTurma;
    private List<Aluno> mListAlunos;
    private TextView tAddAlunoAtividade;
    private LinearLayout lnAlunoAtividade;

    public static void show(FragmentManager fm, Atividade atividade, Turma turma, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_atividade");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarAtividadeDialog frag = new EditarAtividadeDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListAtividade.KEY, atividade);
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "dialog_atividade");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Atualizar Atividade");
        View view = inflater.inflate(R.layout.dialog_atividade, container, false);
        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        
        lnAlunoAtividade = (LinearLayout) view.findViewById(R.id.ln_aluno_atividade);
        tAddAlunoAtividade = (TextView) view.findViewById(R.id.tv_aluno_atividade);
        tAddAlunoAtividade.setOnClickListener(onClickEditarAlunoAtividade());
        
        mHelper = new AtividadeHelper(getActivity(), view);
        this.mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        mAtividade = (Atividade) getArguments().getSerializable(ListAtividade.KEY);
        if (mAtividade != null) {
            mHelper.colocaAtividadeNoFormulario(mAtividade);
            listAlunoAtividade();

            if (mListAlunos == null) {
                mListAlunos = new AlunoDAO(getActivity()).listar(mTurma);
                carregaAlunosSelecionados();
            }
        }

        return view;
    }

    private View.OnClickListener onClickEditarAlunoAtividade() {
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
            public void onClick(View v) {
                Context context = v.getContext();
                AtividadeDAO dao = new AtividadeDAO(context);
                mAtividade = mHelper.pegaAtividadeDoFormulario();
                if (mAtividade == null) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                dao.atualizar(mAtividade);
                dao.close();

                AlunoAtividadeDAO alunoAtividadeDAO = new AlunoAtividadeDAO(context);
                List<AlunoAtividade> alunoAtividadesList = new ArrayList<>();
                for (AlunoAtividade aa : listAlunoAtividade()) {
                    alunoAtividadesList.add(aa);
                    alunoAtividadeDAO.remover(aa.getId());
                }
                alunoAtividadeDAO.close();
                if (mListAlunos != null) {
                    AlunoAtividadeDAO alunoAtivDao = new AlunoAtividadeDAO(context);
                    for (Aluno aluno : mListAlunos) {
                        if (aluno.getStatus().equals("ativo")) {
                            AlunoAtividade alunoAtividade = new AlunoAtividade();
                            alunoAtividade.setAlunoId(aluno.getId());
                            alunoAtividade.setAtividadeId(mAtividade.getId());
                            alunoAtividade.setStatus("feito");
                            for (AlunoAtividade aa : alunoAtividadesList) {
                                if (aluno.getId().equals(aa.getAlunoId()))
                                alunoAtividade.setStatus(aa.getStatus());
                            }
                            alunoAtivDao.inserir(alunoAtividade);
                        }
                    }
                    alunoAtivDao.close();
                }

                if (callback != null) {
                    callback.onAtividadeUpdate(mAtividade);
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
    
    private List<AlunoAtividade> listAlunoAtividade() {
        List<AlunoAtividade> list = carregaAlunosAt();

        AlunoDAO aulaDAO = new AlunoDAO(getActivity());
        for (int i = 0; i < list.size(); i++) {
            Aluno aula = aulaDAO.buscarPorId(list.get(i).getAlunoId());
            TextView tv = new TextView(getActivity());
            tv.setText(aula.getNome() + " " + aula.getSobrenome());
            lnAlunoAtividade.addView(tv);
        }
        
        return list;
    }

    private void carregaAlunosSelecionados() {
        for (int i = 0; i<mListAlunos.size(); i++) {
            mListAlunos.get(i).setStatus("inativo");
            for (AlunoAtividade aa: carregaAlunosAt()) {
                if (mListAlunos.get(i).getId().equals(aa.getAlunoId())) {
                    mListAlunos.get(i).setStatus("ativo");
                }
            }
        }

    }

    private List<AlunoAtividade> carregaAlunosAt() {
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getActivity());
        List<AlunoAtividade> list = dao.listar(mAtividade);
        dao.close();
        return list;
    }

    private void cr (){
        List<AlunoAtividade> alunoAtividades = new ArrayList<>();
        List<Aluno> alunoList = mListAlunos;
        for (AlunoAtividade aa : carregaAlunosAt()){
            for (int i=0; i < alunoList.size(); i++) {
                if (alunoList.get(i).getStatus().equals("ativo")) {
                    if (aa.getAlunoId().equals(alunoList.get(i).getId())) {
                        alunoAtividades.add(aa);
                    }
                } else {
                    alunoList.remove(i);
                }
            }
        }
        for (AlunoAtividade a : alunoAtividades) {
            Log.i("LOG", String.valueOf(a.getAlunoId()));
        }
        Log.i("LOG", "");
        for (Aluno a : alunoList) {
            Log.i("LOG", String.valueOf(a.getId()));
        }
    }

    public interface Callback {
        public void onAtividadeUpdate(Atividade atividade);
    }
}
