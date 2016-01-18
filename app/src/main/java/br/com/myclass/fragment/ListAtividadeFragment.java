package br.com.myclass.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import br.com.myclass.adapter.AtividadeAdapter;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.fragment.dialog.AdicionarAtividadeDialog;
import br.com.myclass.fragment.dialog.DetalhesAtividadeDialog;
import br.com.myclass.fragment.dialog.EditarAtividadeDialog;
import br.com.myclass.fragment.dialog.ListEditarAlunoAtividadeDialog;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAtividade;
import br.com.myclass.model.ListAula;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

public class ListAtividadeFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener{
    private RecyclerView mRecyclerView;
    private AtividadeAdapter mAdapter;
    private List<Atividade> mListAtividades;
    private Button btnCadastrar;
    private ImageButton fabBtn;
    private int position;
    private Aula mAula;
    private Turma mTurma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_atividade, container, false);

        mAula = (Aula) getActivity().getIntent().getSerializableExtra(ListAula.KEY);
        mTurma = (Turma) getActivity().getIntent().getSerializableExtra(ListTurma.KEY);

        setHasOptionsMenu(true);
        setUpToolbar(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(mAula.getConteudo());

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_atividades);
        mRecyclerView.setHasFixedSize(true);

        fab(mRecyclerView, view, fabBtn);
        fabBtn = getFabButton(fabBtn, view);
        fabBtn.setOnClickListener(this);

        swipeDelete();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListAtividade list = (ListAtividade) savedInstanceState.getSerializable(ListAtividade.KEY);
            mListAtividades = list.mAtividades;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAtividade.KEY, new ListAtividade(mListAtividades));
    }

    private void carregaLista() {
        AtividadeDAO dao = new AtividadeDAO(getActivity());

        if (mAula != null) {
            mListAtividades =  dao.listar(mAula, "ativo");
        }

        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AtividadeAdapter(mListAtividades, getActivity(), onClickPopuMenu(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        mostraBtnCadastro();
    }

    private void mostraBtnCadastro() {
        if (mListAtividades.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private AtividadeAdapter.OnClickListener onClickListener() {
        return new AtividadeAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                ListEditarAlunoAtividadeDialog.show(getFragmentManager(), mListAtividades.get(idx), new ListEditarAlunoAtividadeDialog.Callback() {
                    @Override
                    public void onAlunoAtividadeAdd(Atividade atividade) {
                        snackbar("Atividades dos alunos atualizadas com sucesso!", "FECHAR", mRecyclerView);
                    }
                });
            }
        };
    }

    private AtividadeAdapter.PopupMenuOnClickListener onClickPopuMenu() {
        return new AtividadeAdapter.PopupMenuOnClickListener() {
            @Override
            public void onClickMenuPopup(View view, int idx) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                menu.setOnMenuItemClickListener(ListAtividadeFragment.this);
                inflater.inflate(R.menu.menu_popup, menu.getMenu());
                menu.show();
                position = idx;
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        addAtividade();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detalhes:
                DetalhesAtividadeDialog.show(getFragmentManager(), mListAtividades.get(position), mTurma);
                break;
            case R.id.menu_editar:
                EditarAtividadeDialog.show(getFragmentManager(), mListAtividades.get(position), mTurma, new EditarAtividadeDialog.Callback() {
                    @Override
                    public void onAtividadeUpdate(Atividade atividade) {
                        carregaLista();
                        snackbar("Atividade atualizada com sucesso.", "FECHAR", mRecyclerView);
                    }
                });
                break;
            case R.id.menu_arquivar:
                excluir();
                break;
        }
        return true;
    }

    private void addAtividade() {
        AdicionarAtividadeDialog.show(getFragmentManager(), mAula, mTurma, new AdicionarAtividadeDialog.Callback() {
            @Override
            public void onAtividadeAdd(Atividade atividade) {
                carregaLista();
                snackbar("Atividade salva com sucesso.", "FECHAR", mRecyclerView);
            }
        });
    }

    private void excluir() {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                excluirLinha(position);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message("Deseja excluir a atividade " + " " + mListAtividades.get(position).getTipo() + "?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }


    private void excluirLinha(final int position) {
        final Atividade atividade = mListAtividades.get(position);
        arquivar(atividade);
        mAdapter.remove(position);
        mostraBtnCadastro();

        Snackbar.make(mRecyclerView, "Atividade excluida com sucesso.", Snackbar.LENGTH_LONG)
                .setAction("DESFAZER", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desarquivar(atividade);
                        mAdapter.adicionar(position, atividade);
                        mostraBtnCadastro();
                    }
                })
                .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                .show();
    }


    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                excluirLinha(viewHolder.getAdapterPosition());
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    Bitmap bitmap;

                    if (dX > 0) { // swiping right
                        paint.setColor(getResources().getColor(R.color.accent_700));
                        bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_delete);
                        float height = (itemView.getHeight() / 2) - (bitmap.getHeight() / 2);

                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
                        c.drawBitmap(bitmap, 96f, (float) itemView.getTop() + height, null);




                    } else { // swiping left
                        paint.setColor(getResources().getColor(R.color.accent_700));
                        bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_delete);
                        float height = (itemView.getHeight() / 2) - (bitmap.getHeight() / 2);
                        float bitmapWidth = bitmap.getWidth();

                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                        c.drawBitmap(bitmap, ((float) itemView.getRight() - bitmapWidth) - 96f, (float) itemView.getTop() + height, null);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }



        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void arquivar(Atividade atividade) {
        excluirAlunoAtividade(atividade);
        AtividadeDAO dao = new AtividadeDAO(getActivity());
        atividade.setStatus("inativo");
        dao.atualizar(atividade);
        dao.close();

    }

    private void desarquivar(Atividade atividade) {
        AtividadeDAO dao = new AtividadeDAO(getActivity());
        atividade.setStatus("ativo");
        dao.atualizar(atividade);
        dao.close();

    }

    private void excluirAlunoAtividade(Atividade atividade) {
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getContext());
        List<AlunoAtividade> alunoAtividades = dao.listar(atividade);
        for (AlunoAtividade aa : alunoAtividades) {
            dao.remover(aa.getId());
        }
        dao.close();
    }
}
