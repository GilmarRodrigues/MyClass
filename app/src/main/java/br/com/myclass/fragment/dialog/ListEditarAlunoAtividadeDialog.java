package br.com.myclass.fragment.dialog;

import android.content.Context;
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
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.AlunoAtividadeDialogAdapter;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.ListAlunoAtividade;
import br.com.myclass.model.ListAtividade;

/**
 * Created by gilmar on 27/12/15.
 */
public class ListEditarAlunoAtividadeDialog extends BaseDialog {
    private Callback callback;
    private Atividade mAtividade;
    private RecyclerView mRecyclerView;
    private AlunoAtividadeDialogAdapter mAdapter;
    private List<AlunoAtividade> mAlunoAtividadesList;
    private Button btnOk;
    private TextView tMsgAlunosAtividade;

    public static void show(FragmentManager fm, Atividade atividade, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("adicionar_aluno_atividade");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ListEditarAlunoAtividadeDialog frag = new ListEditarAlunoAtividadeDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListAtividade.KEY, atividade);
        frag.setArguments(args);
        frag.show(ft, "adicionar_aluno_atividade");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAtividade = (Atividade) getArguments().getSerializable(ListAtividade.KEY);
        getDialog().setTitle(mAtividade.getTipo());
        View view = inflater.inflate(R.layout.dialog_list_aluno_atividade, container, false);

        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(onClickOk());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_alunos_atividades);
        mRecyclerView.setHasFixedSize(true);

        tMsgAlunosAtividade = (TextView) view.findViewById(R.id.tv_msg_alunos_atividade_vazio);

        return view;
    }

    private View.OnClickListener onClickOk() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callback != null) {
                    callback.onAlunoAtividadeAdd(mAtividade);
                }
                dismiss();
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListAlunoAtividade list = (ListAlunoAtividade) savedInstanceState.getSerializable(ListAlunoAtividade.KEY);
            mAlunoAtividadesList = list.mAlunoAtividades;
        }

        carregaLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAlunoAtividade.KEY, new ListAlunoAtividade(mAlunoAtividadesList));
    }

    private void carregaLista(){
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getContext());
        if (mAtividade != null) {
            mAlunoAtividadesList = dao.listar(mAtividade);
        }
        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AlunoAtividadeDialogAdapter(mAlunoAtividadesList, getActivity(), OnClickListener(), OnClickCheckBox());
        mRecyclerView.setAdapter(mAdapter);

        showMsgAlunosAtividade();

    }

    private void showMsgAlunosAtividade() {
        if (mAlunoAtividadesList.size() <= 0) {
            tMsgAlunosAtividade.setVisibility(View.VISIBLE);
        } else {
            tMsgAlunosAtividade.setVisibility(View.INVISIBLE);
        }
    }

    private AlunoAtividadeDialogAdapter.OnClickCheckBox OnClickCheckBox() {
        return new AlunoAtividadeDialogAdapter.OnClickCheckBox() {
            @Override
            public void onClickCheckBox(View view, int idx) {
                Context context = view.getContext();
                AlunoAtividadeDAO dao = new AlunoAtividadeDAO(context);
                AlunoAtividade alunoAtividade = mAlunoAtividadesList.get(idx);
                if (mAlunoAtividadesList.get(idx).getStatus().equals("feito")){
                    alunoAtividade.setStatus("naofeito");
                }else {
                    alunoAtividade.setStatus("feito");
                }
                dao.atualizar(alunoAtividade);
                dao.close();
            }
        };
    }

    private AlunoAtividadeDialogAdapter.OnClickListener OnClickListener() {
        return new AlunoAtividadeDialogAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                Toast.makeText(getActivity(), "onClick()", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static interface Callback {
        public void onAlunoAtividadeAdd(Atividade atividade);
    }
}
