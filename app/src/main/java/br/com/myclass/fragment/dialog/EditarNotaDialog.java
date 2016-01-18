package br.com.myclass.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.dao.AulaProvaDAO;
import br.com.myclass.dao.NotaDAO;
import br.com.myclass.helper.AtividadeHelper;
import br.com.myclass.helper.NotaHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.Aula;
import br.com.myclass.model.AulaProva;
import br.com.myclass.model.ListNota;
import br.com.myclass.model.ListProva;
import br.com.myclass.model.Nota;
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 30/12/15.
 */
public class EditarNotaDialog extends BaseDialog {
    private Callback callback;
    private Nota mNota;
    private Prova mProva;
    private NotaHelper mHelper;
    private LinearLayout lnAtividades;

    public static void show(FragmentManager fm, Nota nota, Prova prova, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_nota");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarNotaDialog frag = new EditarNotaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListNota.KEY, nota);
        args.putSerializable(ListProva.KEY, prova);
        frag.setArguments(args);
        frag.show(ft, "editar_nota");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNota = (Nota) getArguments().getSerializable(ListNota.KEY);
        mProva = (Prova) getArguments().getSerializable(ListProva.KEY);
        Aluno aluno = getAluno();
        getDialog().setTitle(aluno.getNome() + " " + aluno.getSobrenome());
        View view = inflater.inflate(R.layout.dialog_nota, container, false);
        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        lnAtividades = (LinearLayout) view.findViewById(R.id.ln_atividades);
        mHelper = new NotaHelper(getActivity(), view);

        if (mNota != null) {
            mHelper.colocaNotaNoFormulario(mNota);
        }
        showAtividades();
        return view;
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNota = mHelper.pegaNotaDoFormulario();
                Context context = v.getContext();
                NotaDAO dao = new NotaDAO(context);
                dao.atualizar(mNota);
                dao.close();

                if (callback != null) {
                    callback.onNotaUptade(mNota);
                }
                dismiss();
            }
        };
    }

    private View.OnClickListener onClickCancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    private Aluno getAluno() {
        AlunoDAO dao = new AlunoDAO(getActivity());
        Aluno aluno = dao.buscarPorId(mNota.getAlunoId());
        dao.close();
        return aluno;
    }

    private List<AulaProva> listAulaProva(){
        AulaProvaDAO dao = new AulaProvaDAO(getActivity());
        List<AulaProva> listAulaProva = dao.listar(mProva, "ativo");
        dao.close();
        return listAulaProva;
    }

    private List<Aula> listAulasSelecionadas () {
        AulaDAO dao = new AulaDAO(getActivity());
        List<Aula> listAulas = new ArrayList<>();

        for (AulaProva ap: listAulaProva()) {
            Aula a = dao.buscarPorId(ap.getAulaId());
            listAulas.add(a);
        }
        dao.close();
        return listAulas;
    }

    private List<Atividade> listAtividades() {
        AtividadeDAO dao = new AtividadeDAO(getActivity());
        List<Atividade> atividadeList = new ArrayList<>();
        for (Aula a: listAulasSelecionadas()) {
            List<Atividade> aux = dao.listar(a, "ativo");
            for (Atividade at: aux){
                atividadeList.add(at);
            }
        }
        dao.close();
        return atividadeList;
    }

    private List<AlunoAtividade> listAA() {
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getActivity());
        List<AlunoAtividade> listAlunoAtividade = new ArrayList<>();
        for (Atividade atividade : listAtividades()) {
            List<AlunoAtividade> aux = dao.listar(atividade);
            for (AlunoAtividade a : aux) {
                listAlunoAtividade.add(a);
            }
        }
        dao.close();
        return listAlunoAtividade;
    }

    private void showAtividades() {
        String items[] = new AtividadeHelper().atividades();//new String[6];
        /*items[0] = "Chamada";
        items[1] = "Exercicio na classe";
        items[2] = "Exercicio na para casa";
        items[3] = "Comportamento";
        items[4] = "Seminario";
        items[5] = "Trabalho na classe";*/
        int feito;
        int naofeito;
        int totalFeita = 0;
        int total = 0;
        Aluno aluno = getAluno();
        AtividadeDAO atDAO = new AtividadeDAO(getActivity());
        for (int i = 0; i < items.length; i++) {
            feito = 0;
            naofeito = 0;
            for (AlunoAtividade a : listAA()) {
                if (a.getAlunoId().equals(aluno.getId())){
                    Atividade atividade = atDAO.buscarPorId(a.getAtividadeId());
                    if (items[i].equals(atividade.getTipo())) {
                        if (a.getStatus().equals("feito")) {
                            feito++;
                            totalFeita++;
                        } else {
                            naofeito++;
                        }
                        total++;
                    }
                }
            }
            addAtividade(items[i], feito, (feito + naofeito));
        }
        addAtividade("Total", totalFeita, total);
        poncenAtividadesFeitas(totalFeita, total);
        atDAO.close();
    }

    private void addAtividade(String atividade, int feitas, int naofeitas) {
        TextView tv = new TextView(getActivity());
        tv.setText(atividade + ": " + feitas + "/" + naofeitas);
        lnAtividades.addView(tv);
    }


    private void poncenAtividadesFeitas(int totalFeito, int total) {
        if (total > 0 ) {
            int x = (totalFeito * 100) / total;
            TextView tv = new TextView(getActivity());
            tv.setText(x + "% das atividades concluídas");
            if (x >= 90) {
                tv.setTextColor(getActivity().getResources().getColor(R.color.accent));
            } else if (x < 50) {
                tv.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                tv.setTextColor(getActivity().getResources().getColor(R.color.blue));
            }
            lnAtividades.addView(tv);
        }
    }

    public interface Callback {
        public void onNotaUptade(Nota nota);
    }

}
