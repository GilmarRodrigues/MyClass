package br.com.myclass.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import br.com.myclass.activity.TabActivity;
import br.com.myclass.adapter.TurmaAdapter;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.fragment.dialog.AdicionarTurmaDialog;
import br.com.myclass.fragment.dialog.DetalhesTurmaDialog;
import br.com.myclass.fragment.dialog.EditarTurmaDialog;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

public class ListTurmaFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    protected static final String TAG = "MYCLASS";
    private ImageButton fabBtn;
    private List<Turma> mListTurmas;
    private TurmaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button btnCadastrar;
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_turma, container, false);

        setUpToolbar(view);

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaTurmas);
        mRecyclerView.setHasFixedSize(true);

        fab(mRecyclerView, view, fabBtn);
        fabBtn = getFabButton(fabBtn, view);
        fabBtn.setOnClickListener(this);

        swipeDelete();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListTurma list = (ListTurma) savedInstanceState.getSerializable(ListTurma.KEY);
            this.mListTurmas = list.mTurmas;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListTurma.KEY, new ListTurma(mListTurmas));
    }

    @Override
    public void onResume() {
        super.onResume();
        TurmaDAO dao = new TurmaDAO(getActivity());
        List<Turma> lista = dao.listarAtivas();
        dao.close();
        if (lista.size() > mListTurmas.size()) {
            carregaLista();
        }

    }

    private void carregaLista() {
        TurmaDAO dao = new TurmaDAO(getActivity());
        mListTurmas = dao.listarAtivas();
        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new TurmaAdapter(mListTurmas, getActivity(), onPopupMenu(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);
        mostraBtnCadastro();
    }

    private TurmaAdapter.OnClickListener onClickListener() {
        return new TurmaAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Intent intent = new Intent(getActivity(), TabActivity.class);
                intent.putExtra(ListTurma.KEY, mListTurmas.get(idx));
                startActivity(intent);
            }
        };
    }

    private TurmaAdapter.PopupMenuOnClickListener onPopupMenu() {
        return new TurmaAdapter.PopupMenuOnClickListener() {
            @Override
            public void onClickMenuPopup(View view, int idx) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.inflate(R.menu.menu_popup);
                popup.setOnMenuItemClickListener(ListTurmaFragment.this);
                popup.show();
                position = idx;
            }
        };
    }

    @Override
    public void onClick(View v) {
        AdicionarTurmaDialog.show(getFragmentManager(), new AdicionarTurmaDialog.Callback() {
            @Override
            public void onTurmaAdd(Turma turma) {
                carregaLista();
                snackbar("Classe salvo com sucesso.", "FECHAR", mRecyclerView);
            }
        });
    }

    private void mostraBtnCadastro() {
        if (mListTurmas.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }


    private void arquivarTurma(Turma turma) {
        TurmaDAO dao = new TurmaDAO(getActivity());
        turma.setStatus("inativo");
        dao.atualizar(turma);
        dao.close();

    }

    private void desarquivarTurma(Turma turma) {
        TurmaDAO dao = new TurmaDAO(getActivity());
        turma.setStatus("ativo");
        dao.atualizar(turma);
        dao.close();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detalhes:
                final Turma turma = mListTurmas.get(position);
                DetalhesTurmaDialog.show(getFragmentManager(), turma);
                break;

            case R.id.menu_editar:
                EditarTurmaDialog.show(getFragmentManager(), mListTurmas.get(position), new EditarTurmaDialog.Callback() {
                    @Override
                    public void onTurmaUpdate(Turma turma) {
                        carregaLista();
                        snackbar("Classe atualizada com sucesso.", "FECHAR", mRecyclerView);
                    }
                });
                break;

            case R.id.menu_arquivar:
                this.excluir();
                break;
        }

        return true;
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

        ((SimpleDialog.Builder) builder).message("Deseja excluir a classe " + " " + mListTurmas.get(position).getCurso() + "?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
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

    private void excluirLinha(final int position) {
        final Turma turma = mListTurmas.get(position);
        arquivarTurma(turma);
        mAdapter.remove(position);
        mostraBtnCadastro();

        Snackbar.make(mRecyclerView, "Classe excluida com sucesso.", Snackbar.LENGTH_LONG)
                .setAction("DESFAZER", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desarquivarTurma(turma);
                        mAdapter.adicionar(position, turma);
                        mostraBtnCadastro();
                    }
                })
                .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                .show();
    }

}