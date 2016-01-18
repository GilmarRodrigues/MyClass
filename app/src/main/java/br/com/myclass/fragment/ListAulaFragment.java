package br.com.myclass.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.activity.AtividadeActivity;
import br.com.myclass.adapter.AulaAdapter;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.fragment.dialog.AdicionarAulaDialog;
import br.com.myclass.fragment.dialog.DetalhesAulaDialog;
import br.com.myclass.fragment.dialog.EditarAulaDialog;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAula;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

public class ListAulaFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener{
    private List<Aula> mListAulas;
    private AulaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button btnCadastrar;
    private Turma mTurma;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_aula, container, false);

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_aulas);
        mRecyclerView.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        mTurma = (Turma) intent.getSerializableExtra(ListTurma.KEY);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListAula list = (ListAula) savedInstanceState.getSerializable(ListAula.KEY);
            this.mListAulas = list.mAulas;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAula.KEY, new ListAula(mListAulas));
    }


    public void carregaLista() {
        TurmaDAO turmaDAO = new TurmaDAO(getActivity());
        AulaDAO aulaDAO = new AulaDAO(getActivity());

        if (mTurma != null) {
            Turma turmaSelecionada = turmaDAO.buscarPorId(mTurma.getId());
            mListAulas = aulaDAO.listar(turmaSelecionada);
        }

        aulaDAO.close();
        turmaDAO.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AulaAdapter(mListAulas, getActivity(), onClickPopuMenu(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        mostraBtnCadastro();
    }

    private AulaAdapter.OnClickListener onClickListener() {
        return new AulaAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Intent intent = new Intent(getActivity(), AtividadeActivity.class);
                intent.putExtra(ListAula.KEY, mListAulas.get(idx));
                intent.putExtra(ListTurma.KEY, mTurma);
                startActivity(intent);
            }
        };
    }

    private AulaAdapter.PopupMenuOnClickListener onClickPopuMenu() {
        return new AulaAdapter.PopupMenuOnClickListener() {
            @Override
            public void onClickMenuPopup(View view, int idx) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                menu.setOnMenuItemClickListener(ListAulaFragment.this);
                inflater.inflate(R.menu.menu_popup, menu.getMenu());
                menu.show();
                position = idx;
            }
        };
    }

    @Override
    public void onClick(View v) {
        addAula();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detalhes:
                DetalhesAulaDialog.show(getFragmentManager(), mListAulas.get(position));
                break;
            case R.id.menu_editar:
                EditarAulaDialog.show(getFragmentManager(), mListAulas.get(position), new EditarAulaDialog.Callback() {
                    @Override
                    public void onAulaAdd(Aula aula) {
                        carregaLista();
                        snackbar("Aula atualizada com sucesso!", "FECHAR", mRecyclerView);
                    }
                });
                break;
            case R.id.menu_arquivar:
                excluir();
                break;
        }
        return true;
    }

    private void arquivarAluno(Aula aula) {
        arquivarAtividade(aula);
        AulaDAO dao = new AulaDAO(getActivity());
        aula.setStatus("inativo");
        dao.atualizar(aula);
        dao.close();

    }

    private void desarquivarAluno(Aula aula) {
        AulaDAO dao = new AulaDAO(getActivity());
        aula.setStatus("ativo");
        dao.atualizar(aula);
        desarquivarAtividade(aula);
        dao.close();

    }

    private void arquivarAtividade(Aula aula) {
        AtividadeDAO dao = new AtividadeDAO(getActivity());
        List<Atividade> list = dao.listar(aula, "ativo");
        for (Atividade a : list) {
            a.setStatus("inativo");
            dao.atualizar(a);
        }
        dao.close();
    }

    private void desarquivarAtividade(Aula aula) {
        AtividadeDAO dao = new AtividadeDAO(getActivity());
        List<Atividade> list = dao.listar(aula, "inativo");
        for (Atividade a : list) {
            a.setStatus("ativo");
            dao.atualizar(a);
        }
        dao.close();
    }

    private void mostraBtnCadastro() {
        if (mListAulas.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private void addAula() {
        AdicionarAulaDialog.show(getFragmentManager(), mTurma, new AdicionarAulaDialog.Callback() {
            @Override
            public void onAulaAdd(Aula aula) {
                carregaLista();
            }
        });
    }

    private void excluir() {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                final Aula aula = mListAulas.get(position);
                arquivarAluno(aula);
                mAdapter.remove(position);
                mostraBtnCadastro();

                Snackbar.make(mRecyclerView, "Aula excluida com sucesso.", Snackbar.LENGTH_LONG)
                        .setAction("DESFAZER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                desarquivarAluno(aula);
                                mAdapter.adicionar(position, aula);
                                mostraBtnCadastro();
                            }
                        })
                        .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                        .show();;

                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        ((SimpleDialog.Builder) builder).message("Deseja excluir a aula " + mListAulas.get(position).getConteudo() + "?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }
    
}
