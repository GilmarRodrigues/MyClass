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
import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.AulaDialogAdapter;
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAula;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 28/12/15.
 */
public class ListAddEdAulaProvaDialog extends BaseDialog {
    private Callback callback;
    private Turma mTurma;
    private List<Aula> mListAulas;
    private AulaDialogAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView tMsgAulas;

    public static void show(FragmentManager fm, Turma turma, List<Aula> aulaList, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_conteudo_prova");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ListAddEdAulaProvaDialog frag = new ListAddEdAulaProvaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        if (aulaList != null) {
            args.putSerializable(ListAula.KEY, new ListAula(aulaList));
        }
        frag.setArguments(args);
        frag.show(ft, "editar_conteudo_prova");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Selecione os conteúdos");
        View view = inflater.inflate(R.layout.dialog_list_aula, container, false);

        Button btnSalvar = (Button) view.findViewById(R.id.btn_salvar);
        btnSalvar.setOnClickListener(onClickSalvar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_lista_aulas);
        mRecyclerView.setHasFixedSize(true);
        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);

        tMsgAulas = (TextView) view.findViewById(R.id.tv_msg_aulas_vazio_bd);

        ListAula list = (ListAula) getArguments().getSerializable(ListAula.KEY);
        if (list != null) {
            this.mListAulas = list.mAulas;
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
                    callback.onConteudoProvaAdd(mListAulas);
                }
                dismiss();
            }
        };
    }

    private void carregaLista() {
        AulaDAO dao = new AulaDAO(getActivity());

        if (mListAulas == null) {
            if (mTurma != null) {
                mListAulas = dao.listar(mTurma);
            }
        }
        dao.close();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AulaDialogAdapter(mListAulas, getActivity(), onClickCheckBox(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        showMsgAulas();
    }

    private void showMsgAulas() {
        if (mListAulas.size() <= 0) {
            tMsgAulas.setVisibility(View.VISIBLE);
        } else {
            tMsgAulas.setVisibility(View.INVISIBLE);
        }
    }

    private AulaDialogAdapter.OnClickCheckBox onClickCheckBox() {
        return new AulaDialogAdapter.OnClickCheckBox() {
            @Override
            public void OnClickCheckBox(View view, int idx) {
                if (mListAulas.get(idx).getStatus().equals("ativo")) {
                    mListAulas.get(idx).setStatus("inativo");
                } else {
                    mListAulas.get(idx).setStatus("ativo");
                }
            }
        };
    }

    private AulaDialogAdapter.OnClickListener onClickListener() {
        return new AulaDialogAdapter.OnClickListener() {
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
            ListAula list = (ListAula) savedInstanceState.getSerializable(ListAula.KEY);
            this.mListAulas = list.mAulas;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListAula.KEY, new ListAula(mListAulas));
    }

    // Interface para retornar o resultado
    public static interface Callback {
        public void onConteudoProvaAdd(List<Aula> aulaLis);
    }
}
