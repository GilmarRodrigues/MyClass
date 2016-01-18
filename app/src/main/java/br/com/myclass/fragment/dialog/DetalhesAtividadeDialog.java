package br.com.myclass.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.helper.AtividadeHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.ListAtividade;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 27/12/15.
 */
public class DetalhesAtividadeDialog extends BaseDialog {
    private Atividade mAtividade;
    private Turma mTurma;
    private AtividadeHelper mHelper;
    private TextView tAddAlunoAtividade;
    private LinearLayout lnAlunoAtividade;

    public static void show(FragmentManager fm, Atividade atividade, Turma turma) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_atividade");
        if (prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesAtividadeDialog frag = new DetalhesAtividadeDialog();
        Bundle args = new Bundle();
        args.putSerializable(ListAtividade.KEY, atividade);
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "detalhes_atividade");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Detalhes da Atividade");
        View view = inflater.inflate(R.layout.dialog_atividade, container, false);
        mHelper = new AtividadeHelper(getActivity(), view);

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);

        lnAlunoAtividade = (LinearLayout) view.findViewById(R.id.ln_aluno_atividade);
        tAddAlunoAtividade = (TextView) view.findViewById(R.id.tv_aluno_atividade);
        tAddAlunoAtividade.setText("Alunos");
        tAddAlunoAtividade.setCompoundDrawables(null, null, null, null);

        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        mAtividade = (Atividade) getArguments().getSerializable(ListAtividade.KEY);
        if (mAtividade != null) {
            mHelper.colocaAtividadeNoFormulario(mAtividade);
            mHelper.offCampos();
            listAlunos();
        }

        return view;
    }

    private List<AlunoAtividade> listAlunosAtividades() {
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getContext());
        List<AlunoAtividade> list = dao.listar(mAtividade);
        dao.close();
        return list;
    }

    private void listAlunos(){
        AlunoDAO dao = new AlunoDAO(getContext());
        List<Aluno> alunoList = dao.listar(mTurma);
        for (AlunoAtividade aa : listAlunosAtividades()) {
            for (int i = 0; i < alunoList.size(); i++) {
                if (aa.getAlunoId().equals(alunoList.get(i).getId())) {
                    TextView tv = new TextView(getActivity());
                    tv.setText(alunoList.get(i).getNome() + " " + alunoList.get(i).getSobrenome());
                    lnAlunoAtividade.addView(tv);
                }
            }
        }
        dao.close();
    }
}
