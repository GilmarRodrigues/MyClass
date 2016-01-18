package br.com.myclass.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.myclass.R;
import br.com.myclass.helper.HorarioHelper;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListHorario;

/**
 * Created by gilmar on 04/01/16.
 */
public class DetalhesHorarioDialog extends BaseDialog{
    private Horario mHorario;
    private HorarioHelper mHelper;
    public static void show(FragmentManager fm, Horario horario) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_horario");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesHorarioDialog frag = new DetalhesHorarioDialog();
        Bundle args = new Bundle();
        args.putSerializable(ListHorario.KEY, horario);
        frag.setArguments(args);
        frag.show(ft, "detalhes_horario");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Horário");
        View view = inflater.inflate(R.layout.dialog_horario, container, false);
        mHelper = new HorarioHelper(getActivity(), view);

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);

        mHorario = (Horario) getArguments().getSerializable(ListHorario.KEY);
        if (mHorario != null) {
            mHelper.colocaHorarioNoFormulario(mHorario);
            mHelper.offCampos();
        }
        return view;
    }
}
