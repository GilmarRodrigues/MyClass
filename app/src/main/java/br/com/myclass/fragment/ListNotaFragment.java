package br.com.myclass.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.NotaAdapter;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.dao.NotaDAO;
import br.com.myclass.fragment.dialog.EditarNotaDialog;
import br.com.myclass.fragment.dialog.ImgAlunoDialog;
import br.com.myclass.fragment.dialog.ListAdicionarAlunoNotaDialog;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListNota;
import br.com.myclass.model.ListProva;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Nota;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListNotaFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private List<Nota> mListNotas;
    private NotaAdapter mAdapter;
    private Button btnCadastrar;
    private ImageButton fabBtn;
    private Turma mTurma;
    private Prova mProva;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_nota, container, false);

        mTurma = (Turma) getActivity().getIntent().getSerializableExtra(ListTurma.KEY);
        mProva = (Prova) getActivity().getIntent().getSerializableExtra(ListProva.KEY);

        setUpToolbar(view);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(mProva.getDescricao());

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_notas);
        mRecyclerView.setHasFixedSize(true);


        fab(mRecyclerView, view, fabBtn);
        fabBtn = getFabButton(fabBtn, view);
        fabBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListNota list = (ListNota) savedInstanceState.getSerializable(ListNota.KEY);
            mListNotas = list.mNotas;
        }
        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListNota.KEY, new ListNota(mListNotas));
    }

    @Override
    public void onClick(View v) {
        ListAdicionarAlunoNotaDialog.show(getFragmentManager(), mTurma, mListNotas, mProva, new ListAdicionarAlunoNotaDialog.Callback() {
            @Override
            public void onAddAlunoNota(List<Aluno> alunoList) {
                snackbar("Alunos adicionados com sucesso!", "FECHAR", mRecyclerView);
                carregaLista();
            }
        });
    }

    private void carregaLista() {
        listNotas();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new NotaAdapter(mListNotas, getActivity(), onClickListener(), imgOnClickListener(), onClickDelete());
        mRecyclerView.setAdapter(mAdapter);

        mostraBtnCadastro();
    }

    private NotaAdapter.DeleteOnClickListener onClickDelete() {
        return new NotaAdapter.DeleteOnClickListener() {
            @Override
            public void onClickDelete(View view, int idx) {
                excluir(idx);
            }
        };
    }

    private List<Nota> listNotas() {
        NotaDAO dao = new NotaDAO(getActivity());
        if (mProva != null)
            mListNotas = dao.listar(mProva, "ativo");
        dao.close();
        return mListNotas;
    }

    private void mostraBtnCadastro() {
        if (mListNotas.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private NotaAdapter.OnClickListener onClickListener() {
        return new NotaAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                EditarNotaDialog.show(getFragmentManager(), mListNotas.get(idx), mProva, new EditarNotaDialog.Callback() {
                    @Override
                    public void onNotaUptade(Nota nota) {
                        carregaLista();
                        snackbar("Nota atualizada com sucesso!", "FECHAR", mRecyclerView);
                    }
                });
            }
        };
    }

    private NotaAdapter.ImgOnClickListener imgOnClickListener() {
        return new NotaAdapter.ImgOnClickListener() {
            @Override
            public void onClickImg(View view, int idx) {
                ImgAlunoDialog.show(getFragmentManager(), getAluno(idx));
            }
        };
    }

    private Aluno getAluno(int idx){
        AlunoDAO dao = new AlunoDAO(getActivity());
        Aluno aluno= dao.buscarPorId(mListNotas.get(idx).getAlunoId());
        dao.close();
        return aluno;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void excluir(final int position) {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                NotaDAO dao = new NotaDAO(getActivity());
                dao.remover(mListNotas.get(position).getId());
                dao.close();
                carregaLista();
                snackbar("Nota excluida com sucesso.", "FECHAR", mRecyclerView);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message("Deseja excluir a nota do aluno " + getAluno(position).getNome() +  "?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }
}
