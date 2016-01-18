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

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.activity.NotaActivity;
import br.com.myclass.adapter.ProvaAdapter;
import br.com.myclass.dao.ProvaDAO;
import br.com.myclass.fragment.dialog.AdicionarProvaDialog;
import br.com.myclass.fragment.dialog.DetalhesProvaDialog;
import br.com.myclass.fragment.dialog.EditarProvaDialog;
import br.com.myclass.model.ListProva;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

public class ListProvaFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener{
    private List<Prova> mListProvas;
    private ProvaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button btnCadastrar;
    private Turma mTurma;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_prova, container, false);

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_provas);
        mRecyclerView.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        mTurma = (Turma) intent.getSerializableExtra(ListTurma.KEY);
        
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListProva list = (ListProva) savedInstanceState.getSerializable(ListProva.KEY);
            this.mListProvas = list.mProvas;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListProva.KEY, new ListProva(mListProvas));
    }

    public void carregaLista() {
        ProvaDAO aulaDAO = new ProvaDAO(getActivity());

        if (mTurma != null) {
            mListProvas = aulaDAO.listar(mTurma, "ativo");
        }

        aulaDAO.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new ProvaAdapter(mListProvas, getActivity(), onClickPopuMenu(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        mostraBtnCadastro();
    }

    private ProvaAdapter.OnClickListener onClickListener() {
        return new ProvaAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Intent intent = new Intent(getActivity(), NotaActivity.class);
                intent.putExtra(ListProva.KEY, mListProvas.get(idx));
                intent.putExtra(ListTurma.KEY, mTurma);
                startActivity(intent);
            }
        };
    }

    private ProvaAdapter.PopupMenuOnClickListener onClickPopuMenu() {
        return new ProvaAdapter.PopupMenuOnClickListener() {
            @Override
            public void onClickMenuPopup(View view, int idx) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                menu.setOnMenuItemClickListener(ListProvaFragment.this);
                inflater.inflate(R.menu.menu_popup, menu.getMenu());
                menu.show();
                position = idx;
            }
        };
    }

    @Override
    public void onClick(View v) {
        addProva();
    }

    private void addProva() {
        AdicionarProvaDialog.show(getFragmentManager(), mTurma, new AdicionarProvaDialog.Callback() {
            @Override
            public void onProvaAdd(Prova prova) {
                snackbar("Avaliação salva com sucesso!", "FECHAR", mRecyclerView);
                carregaLista();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detalhes:
                DetalhesProvaDialog.show(getFragmentManager(), mListProvas.get(position));
                break;
            case R.id.menu_editar:
                EditarProvaDialog.show(getFragmentManager(), mListProvas.get(position), mTurma, new EditarProvaDialog.Callback() {
                    @Override
                    public void onProvaUpdate(Prova prova) {
                        snackbar("Avaliação atualizada com sucesso!", "FECHAR", mRecyclerView);
                        carregaLista();
                    }
                });
                break;
            case R.id.menu_arquivar:
                excluir();
                break;
        }
        return true;
    }

    private void excluir() {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {

                final Prova prova = mListProvas.get(position);
                arquivarProva(prova);
                mAdapter.remove(position);
                mostraBtnCadastro();

                Snackbar.make(mRecyclerView, "Avaliação excluido com sucesso.", Snackbar.LENGTH_LONG)
                        .setAction("DESFAZER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                desarquivarProva(prova);
                                mAdapter.adicionar(position, prova);
                                mostraBtnCadastro();
                            }
                        })
                        .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                        .show();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message("Deseja excluir a avaliação " + mListProvas.get(position).getDescricao() + "?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    private void desarquivarProva(Prova prova) {
        ProvaDAO dao = new ProvaDAO(getActivity());
        prova.setStatus("ativo");
        dao.atualizar(prova);
        dao.close();
    }

    private void arquivarProva(Prova prova) {
        ProvaDAO dao = new ProvaDAO(getActivity());
        prova.setStatus("inativo");
        dao.atualizar(prova);
        dao.close();
    }

    private void mostraBtnCadastro() {
        if (mListProvas.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }
}