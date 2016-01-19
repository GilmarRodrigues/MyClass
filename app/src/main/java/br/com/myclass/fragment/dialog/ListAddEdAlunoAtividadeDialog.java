package br.com.myclass.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.AlunoDialogAdapter;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListAluno;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 01/01/16.
 */
public class ListAddEdAlunoAtividadeDialog extends BaseDialog {
    private Callback callback;
    private Turma mTurma;
    private List<Aluno> mListAlunos;
    private AlunoDialogAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView tMsgAlunos;
    public CheckBox cbAtividade;

    public static void show(FragmentManager fm, Turma turma, List<Aluno> alunoList, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_aluno_atividade");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ListAddEdAlunoAtividadeDialog frag = new ListAddEdAlunoAtividadeDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        if (alunoList != null) {
            args.putSerializable(ListAluno.KEY, new ListAluno(alunoList));
        }
        frag.setArguments(args);
        frag.show(ft, "editar_aluno_atividade");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Adicionar Alunos");
        View view = inflater.inflate(R.layout.dialog_list_aluno, container, false);

        Button btnSalvar = (Button) view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(onClickSalvar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_alunos);
        mRecyclerView.setHasFixedSize(true);
        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);

        tMsgAlunos = (TextView) view.findViewById(R.id.tv_msg_alunos_vazio_bd);
        cbAtividade = (CheckBox) view.findViewById(R.id.cb_aluno_atividade);

        ListAluno list = (ListAluno) getArguments().getSerializable(ListAluno.KEY);
        if (list != null) {
            this.mListAlunos = list.mAlunos;
        }

        carregaLista();

        return view;
    }

    private View.OnClickListener onClickCancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    private View.OnClickListener onClickSalvar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onAddAlunoAtividade(mListAlunos);
                }
                dismiss();
            }
        };
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(getActivity());

        if (mListAlunos == null) {
            if (mTurma != null) {
                mListAlunos = dao.listar(mTurma);
            }
        }
        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AlunoDialogAdapter(mListAlunos, getActivity(), onClickCheckBox(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        showMsgAlunos();
    }

    private void showMsgAlunos() {
        if (mListAlunos.size() <= 0) {
            tMsgAlunos.setVisibility(View.VISIBLE);
        } else {
            tMsgAlunos.setVisibility(View.INVISIBLE);
        }
    }

    private AlunoDialogAdapter.OnClickCheckBox onClickListener() {
        return new AlunoDialogAdapter.OnClickCheckBox() {
            @Override
            public void onClickCheckBox(View view, int idx) {
                if (mListAlunos.get(idx).getStatus().equals("ativo")) {
                    mListAlunos.get(idx).setStatus("inativo");
                } else {
                    mListAlunos.get(idx).setStatus("ativo");
                }
            }
        };
    }

    private AlunoDialogAdapter.OnClickListener onClickCheckBox() {
        return new AlunoDialogAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Log.i("LOG", "OnClickListener");
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListAluno list = (ListAluno) savedInstanceState.getSerializable(ListAluno.KEY);
            this.mListAlunos = list.mAlunos;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAluno.KEY, new ListAluno(mListAlunos));
    }

    public interface Callback {
        public void onAddAlunoAtividade(List<Aluno> alunoList);
    }

}
