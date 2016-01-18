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
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.AlunoNotaAdapter;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.dao.NotaDAO;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListAluno;
import br.com.myclass.model.ListNota;
import br.com.myclass.model.ListProva;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Nota;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 31/12/15.
 */
public class ListAdicionarAlunoNotaDialog extends BaseDialog {
    private Callback callback;
    private Turma mTurma;
    private RecyclerView mRecyclerView;
    private AlunoNotaAdapter mAdapter;
    private List<Aluno> mAlunoList;
    private List<Nota> mNotaList;
    private Prova mProva;
    private Button btnOk;
    private TextView tMsgAlunos;
    private Button btnAddTodosAlunos;

    public static void show(FragmentManager fm, Turma turma, List<Nota> notas, Prova prova, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("adicionar_aluno_nota");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ListAdicionarAlunoNotaDialog frag = new ListAdicionarAlunoNotaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        args.putSerializable(ListProva.KEY, prova);
        args.putSerializable(ListNota.KEY, new ListNota(notas));
        frag.setArguments(args);
        frag.show(ft, "adicionar_aluno_nota");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListAluno list = (ListAluno) savedInstanceState.getSerializable(ListAluno.KEY);
            mAlunoList = list.mAlunos;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAluno.KEY, new ListAluno(mAlunoList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Adicionar Alunos");
        View view = inflater.inflate(R.layout.dialog_list_aluno_nota, container, false);

        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(onClickOk());
        tMsgAlunos = (TextView) view.findViewById(R.id.tv_msg_alunos_cad);

        btnAddTodosAlunos = (Button) view.findViewById(R.id.btn_salvar_todos);
        btnAddTodosAlunos.setOnClickListener(onClickAddTodos());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_alunos_notas);
        mRecyclerView.setHasFixedSize(true);

        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        mProva = (Prova) getArguments().getSerializable(ListProva.KEY);
        ListNota list = (ListNota) getArguments().getSerializable(ListNota.KEY);
        if (list != null) {
            this.mNotaList = list.mNotas;
        }

        return view;
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(getActivity());
        if (mTurma != null) {
            mAlunoList = dao.listar(mTurma);
        }
        dao.close();

        for (Nota a : mNotaList) {
            for (int i=0; i < mAlunoList.size(); i++) {
                if (a.getAlunoId().equals(mAlunoList.get(i).getId())) {
                    mAlunoList.remove(i);
                }
            }
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AlunoNotaAdapter(mAlunoList, getActivity(), OnClickListener(), OnClickCheckBox());
        mRecyclerView.setAdapter(mAdapter);

        showMsgAlunos();
    }

    private void listNota() {
        NotaDAO dao = new NotaDAO(getContext());
        mNotaList = dao.listar(mProva, "ativo");
        dao.close();
    }

    private void showMsgAlunos() {
        listNota();
        if (mAlunoList.size() <= 0) {
            tMsgAlunos.setVisibility(View.VISIBLE);
        } else {
            tMsgAlunos.setVisibility(View.INVISIBLE);
        }

        if (mAlunoList.size() == 0 && mNotaList.size() != 0) {
            tMsgAlunos.setText(R.string.msg_vazio_list_alunos);
        } else {
            tMsgAlunos.setText(R.string.msg_vazio_list_alunos_bd);
        }
    }

    private AlunoNotaAdapter.OnClickCheckBox OnClickCheckBox() {
        return new AlunoNotaAdapter.OnClickCheckBox() {
            @Override
            public void onClickCheckBox(View view, int idx) {
                NotaDAO dao = new NotaDAO(view.getContext());
                Nota nota = new Nota();
                nota.setNota("");
                nota.setPontoExtra("");
                nota.setStatus("ativo");
                nota.setAlunoId(mAlunoList.get(idx).getId());
                nota.setProvaId(mProva.getId());
                dao.inserir(nota);
                dao.close();
                mAdapter.remove(idx);
                showMsgAlunos();

                Toast.makeText(view.getContext(), "Aluno adicionado com sucesso", Toast.LENGTH_SHORT).show();

                if (callback != null) {
                    callback.onAddAlunoNota(mAlunoList);
                }
            }
        };
    }

    private View.OnClickListener onClickAddTodos() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotaDAO dao = new NotaDAO(view.getContext());
                for (int i=0; i < mAlunoList.size(); i++) {
                    Nota nota = new Nota();
                    nota.setNota("");
                    nota.setPontoExtra("");
                    nota.setStatus("ativo");
                    nota.setAlunoId(mAlunoList.get(i).getId());
                    nota.setProvaId(mProva.getId());
                    dao.inserir(nota);
                }
                dao.close();
                showMsgAlunos();

                if (callback != null) {
                    callback.onAddAlunoNota(mAlunoList);
                }
                dismiss();
            }
        };
    }

    private AlunoNotaAdapter.OnClickListener OnClickListener() {
        return new AlunoNotaAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Log.i("MYCLASS", mAlunoList.get(idx).getNome());
            }
        };
    }

    private View.OnClickListener onClickOk() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        };
    }

    public interface Callback {
        public void onAddAlunoNota(List<Aluno> alunoList);
    }
}
