package br.com.myclass.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import br.com.myclass.adapter.HorarioAdapter;
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.fragment.dialog.AdicionarHorarioDialog;
import br.com.myclass.fragment.dialog.DetalhesHorarioDialog;
import br.com.myclass.fragment.dialog.EditarHorarioDialog;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListHorario;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;


public class ListHorarioFragment extends BaseFragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener{
    private RecyclerView mRecyclerView;
    private HorarioAdapter mAdapter;
    private List<Horario> mListHorarios;
    private Turma mTurma;
    private Button btnCadastrar;
    private ImageButton fabBtn;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_horario, container, false);

        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_horario);
        mRecyclerView.setHasFixedSize(true);

        btnCadastrar = (Button) view.findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(this);

        fab(mRecyclerView, view, fabBtn);
        fabBtn = getFabButton(fabBtn, view);
        fabBtn.setOnClickListener(this);

        mTurma = (Turma) getActivity().getIntent().getSerializableExtra(ListTurma.KEY);

        swipeDelete();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListHorario list = (ListHorario) savedInstanceState.getSerializable(ListHorario.KEY);
            mListHorarios = list.mHorarios;
        }

        carregarLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListHorario.KEY, new ListHorario(mListHorarios));
    }

    private void carregarLista () {
        mListHorarios = new HorarioDAO(getContext()).listar("ativo");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new HorarioAdapter(mListHorarios, getActivity(), onClickListener(), onClickPopuMenu());
        mRecyclerView.setAdapter(mAdapter);

        mostraBtnCadastro();
    }

    private void mostraBtnCadastro() {
        if (mListHorarios.size() <= 0) {
            btnCadastrar.setVisibility(View.VISIBLE);
        } else {
            btnCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private HorarioAdapter.PopupMenuOnClickListener onClickPopuMenu() {
        return new HorarioAdapter.PopupMenuOnClickListener() {
            @Override
            public void onClickMenuPopup(View view, int idx) {
                PopupMenu menu = new PopupMenu(getActivity(), view);
                MenuInflater inflater = menu.getMenuInflater();
                menu.setOnMenuItemClickListener(ListHorarioFragment.this);
                inflater.inflate(R.menu.menu_popup_two, menu.getMenu());
                menu.show();
                position = idx;
            }
        };
    }

    private HorarioAdapter.OnClickListener onClickListener() {
        return new HorarioAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                DetalhesHorarioDialog.show(getFragmentManager(), mListHorarios.get(idx));
            }
        };
    }


    @Override
    public void onClick(View v) {
        AdicionarHorarioDialog.show(getFragmentManager(), new AdicionarHorarioDialog.Callback() {
            @Override
            public void onAddHorario(Horario horario) {
                snackbar("Horário salvo com sucesso!", "FECHAR", mRecyclerView);
                carregarLista();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar:
                EditarHorarioDialog.show(getFragmentManager(), mListHorarios.get(position), new EditarHorarioDialog.Callback() {
                    @Override
                    public void onUpdateHorario(Horario horario) {
                        snackbar("Horário atualizado com sucesso!", "FECHAR", mRecyclerView);
                        carregarLista();
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
                excluirLinha(position);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message("Deseja excluir o horário do dia " + mListHorarios.get(position).getDia() +
                                                 ", hora inicial da aula "+ mListHorarios.get(position).getHoraInicio() +
                                                 " e hora encerramento da aula " + mListHorarios.get(position).getHoraFim()+"?")
                .title("Atenção!")
                .positiveAction("EXCLUIR")
                .negativeAction("CANCELAR");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
        final Horario horario = mListHorarios.get(position);
        arquivarHorario(horario);
        mAdapter.remove(position);
        mostraBtnCadastro();

        Snackbar.make(mRecyclerView, "Classe excluida com sucesso.", Snackbar.LENGTH_LONG)
                .setAction("DESFAZER", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desarquivarHorario(horario);
                        mAdapter.adicionar(position, horario);
                        mostraBtnCadastro();
                    }
                })
                .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                .show();
    }

    private void arquivarHorario(Horario horario) {
        HorarioDAO dao = new HorarioDAO(getActivity());
        horario.setStatus("inativo");
        dao.atualizar(horario);
        dao.close();

    }

    private void desarquivarHorario(Horario horario) {
        HorarioDAO dao = new HorarioDAO(getActivity());
        horario.setStatus("ativo");
        dao.atualizar(horario);
        dao.close();

    }
}