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
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.helper.TurmaHelper;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;


public class DetalhesTurmaDialog extends BaseDialog {
    private Turma turma;
    private TurmaHelper mHelper;
    private LinearLayout lnHorario;
    private TextView tAddHorario;

    public static void show(FragmentManager fm, Turma turma) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_turma");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesTurmaDialog frag = new DetalhesTurmaDialog();
        Bundle args = new Bundle();
        // Passa o objeto por parâmetro
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "detalhes_turma");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.title_activity_tela_detalhes_turma);
        View view = inflater.inflate(R.layout.dialog_turma, container, false);

        mHelper = new TurmaHelper(getActivity(), view);

        lnHorario = (LinearLayout) view.findViewById(R.id.ln_horario);
        tAddHorario = (TextView) view.findViewById(R.id.tv_horario);
        tAddHorario.setText("Horário");
        tAddHorario.setCompoundDrawables(null, null, null, null);

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);
        view.findViewById(R.id.tv_horario_excluir).setVisibility(View.GONE);

        turma = (Turma) getArguments().getSerializable(ListTurma.KEY);

        view.findViewById(R.id.tv_horario_excluir).setVisibility(View.GONE);

        if (turma != null) {
            mHelper.colocaTurmaNoFormulario(turma);
            mHelper.offCampos();
            addHorario();
        }
        return view;
    }

    private List<Horario> horarioList() {
        List<Horario> horarioList = new HorarioDAO(getActivity()).listar(turma, "ativo");
        return horarioList;
    }

    private void addHorario() {
        for (Horario h : horarioList()) {
            TextView tv = new TextView(getActivity());
            tv.setText(h.getDia() + ",  " +h.getHoraInicio() + " às " + h.getHoraFim());
            lnHorario.addView(tv);
        }
    }

}
