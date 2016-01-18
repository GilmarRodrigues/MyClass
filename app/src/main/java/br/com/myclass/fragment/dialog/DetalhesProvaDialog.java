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
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.dao.AulaProvaDAO;
import br.com.myclass.helper.ProvaHelper;
import br.com.myclass.model.Aula;
import br.com.myclass.model.AulaProva;
import br.com.myclass.model.ListProva;
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 29/12/15.
 */
public class DetalhesProvaDialog extends BaseDialog{
    private Prova mProva;
    private ProvaHelper mHelper;
    private TextView tContProva;
    private LinearLayout lnConteudoProva;

    public static void show(FragmentManager fm, Prova prova) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_prova");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesProvaDialog frag = new DetalhesProvaDialog();
        Bundle args = new Bundle();
        args.putSerializable(ListProva.KEY, prova);
        frag.setArguments(args);
        frag.show(ft, "detalhes_prova");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Detalhes da Avaliação");
        View view = inflater.inflate(R.layout.dialog_prova, container, false);
        lnConteudoProva = (LinearLayout) view.findViewById(R.id.ln_conteudo_prova);

        tContProva = (TextView) view.findViewById(R.id.tv_conteudo_prova);
        tContProva.setText("Conteúdo da Prova");
        tContProva.setCompoundDrawables(null, null, null, null);

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);

        mProva = (Prova) getArguments().getSerializable(ListProva.KEY);
        mHelper = new ProvaHelper(getActivity(), view);

        if (mProva != null) {
            mHelper.colocaProvaNoFormulario(mProva);
            mHelper.offCampos();
            listAulaProva();
        }

        return view;
    }

    private List<AulaProva> listAulaProva () {
        AulaProvaDAO dao = new AulaProvaDAO(getActivity());
        List<AulaProva> list = dao.listar(mProva, "ativo");

        AulaDAO aulaDAO = new AulaDAO(getActivity());
        for (int i = 0; i < list.size(); i++) {
            Aula aula = aulaDAO.buscarPorId(list.get(i).getAulaId());
            if (list.get(i).getStatus().equals("ativo")) {
                TextView tv = new TextView(getActivity());
                tv.setText(aula.getConteudo());
                lnConteudoProva.addView(tv);
            }
        }
        aulaDAO.close();
        dao.close();
        return list;
    }
}
