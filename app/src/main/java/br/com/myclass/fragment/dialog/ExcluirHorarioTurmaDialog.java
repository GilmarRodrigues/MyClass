package br.com.myclass.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.HorarioTurmaDialogAdapter;
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListHorario;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 06/01/16.
 */
public class ExcluirHorarioTurmaDialog extends BaseDialog {
    private Callback callback;
    private Turma mTurma;
    private RecyclerView mRecyclerView;
    private List<Horario> mHorarioList;
    private HorarioTurmaDialogAdapter mAdapter;
    private TextView tMsgHorarios;

    public static void show(FragmentManager fm, Turma turma, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("excluir_horario_turma");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ExcluirHorarioTurmaDialog frag = new ExcluirHorarioTurmaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "excluir_horario_turma");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        // Atualiza o tamanho do dialog
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_heigth);
        //getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Excluir Horário");
        View view = inflater.inflate(R.layout.dialog_list_excluir_horario, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_horario);
        mRecyclerView.setHasFixedSize(true);

        Button btnExcluir = (Button) view.findViewById(R.id.btn_excluir);
        btnExcluir.setOnClickListener(onClickExckuir());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_horario);
        mRecyclerView.setHasFixedSize(true);
        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);

        tMsgHorarios = (TextView) view.findViewById(R.id.tv_msg_horario_vazio_bd);

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

    private View.OnClickListener onClickExckuir() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorarioDAO dao = new HorarioDAO(getActivity());
                for (Horario horario: mHorarioList) {
                    if (horario.getStatus().equals("inativo")) {
                        dao.remover(horario.getId());
                    }
                }
                dao.close();

                if (callback != null) {
                    callback.onDeleteHorario(mHorarioList);
                }
                dismiss();
            }
        };
    }

    private void carregaList() {
        HorarioDAO dao = new HorarioDAO(getActivity());
        
        if (mTurma != null) {
            mHorarioList = dao.listar(mTurma, "ativo");
        }
        
        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new HorarioTurmaDialogAdapter(mHorarioList, getActivity(), onClickCheckBox(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        showMsgHorarios();
    }

    private void showMsgHorarios() {
        if (mHorarioList.size() <= 0) {
            tMsgHorarios.setVisibility(View.VISIBLE);
        } else {
            tMsgHorarios.setVisibility(View.INVISIBLE);
        }
    }

    private HorarioTurmaDialogAdapter.OnClickCheckBox onClickListener() {
        return new HorarioTurmaDialogAdapter.OnClickCheckBox() {
            @Override
            public void onClickCheckBox(View view, int idx) {
                if (mHorarioList.get(idx).getStatus().equals("ativo")) {
                    mHorarioList.get(idx).setStatus("inativo");
                } else {
                    mHorarioList.get(idx).setStatus("ativo");
                }
            }
        };
    }

    private HorarioTurmaDialogAdapter.OnClickListener onClickCheckBox() {
        return new HorarioTurmaDialogAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListHorario list = (ListHorario) savedInstanceState.getSerializable(ListHorario.KEY);
            this.mHorarioList = list.mHorarios;
        }
        carregaList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListHorario.KEY, new ListHorario(mHorarioList));
    }

    public interface Callback {
        public void onDeleteHorario(List<Horario> horarioList);
    }

}
