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
import br.com.myclass.helper.AulaHelper;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAula;

/**
 * Created by gilmar on 22/12/15.
 */
public class DetalhesAulaDialog extends BaseDialog {
    private Aula aula;
    private AulaHelper mHelper;

    public static void show(FragmentManager fm, Aula aula) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_aula");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesAulaDialog frag = new DetalhesAulaDialog();
        Bundle args = new Bundle();
        // Passa o objeto por parâmetro
        args.putSerializable(ListAula.KEY, aula);
        frag.setArguments(args);
        frag.show(ft, "detalhes_aula");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Detalhes Aula");
        View view = inflater.inflate(R.layout.dialog_aula, container, false);
        mHelper = new AulaHelper(getActivity(), view);

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);

        aula = (Aula) getArguments().getSerializable(ListAula.KEY);

        if (aula != null) {
            mHelper.colocaAulaNoFormulario(aula);
            mHelper.offCampos();
        }
        return view;
    }
}
