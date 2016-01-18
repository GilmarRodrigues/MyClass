package br.com.myclass.fragment.dialog;

import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.dao.NotaDAO;
import br.com.myclass.helper.AtividadeHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.model.ListAluno;
import br.com.myclass.model.Nota;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 01/01/16.
 */
public class DadosAlunoDialog extends BaseDialog{
    private Aluno mAluno;
    private LinearLayout lnAtividades;
    private LinearLayout lnNotas;

    public static void show(FragmentManager fm, Aluno aluno) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dados_aluno");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DadosAlunoDialog frag = new DadosAlunoDialog();
        Bundle args = new Bundle();
        args.putSerializable(ListAluno.KEY, aluno);
        frag.setArguments(args);
        frag.show(ft, "dados_aluno");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Dados do Aluno");
        View view = inflater.inflate(R.layout.dialog_dados_aluno, container, false);
        mAluno = (Aluno) getArguments().getSerializable(ListAluno.KEY);
        lnAtividades = (LinearLayout) view.findViewById(R.id.ln_atividades);
        lnNotas = (LinearLayout) view.findViewById(R.id.ln_notas);
        TextView tNome = (TextView) view.findViewById(R.id.tv_nome);
        RoundedImageView ivFoto = (RoundedImageView) view.findViewById(R.id.iv_foto);
        ivFoto.setOnClickListener(onClickFoto());
        if (mAluno != null) {
            tNome.setText(mAluno.getNome() + " " + mAluno.getSobrenome());
            listAtividades();
            listNotas();

            if (mAluno.getFoto() != null) {
                File file = new File(mAluno.getFoto());
                ImageUtils.carregaImagemCamera(file, mAluno, ivFoto.getWidth(), ivFoto.getHeight(), ivFoto);
            } else if (mAluno.getGaleria() != null) {
                try {
                    ImageUtils.carregaImagemGaleria(Uri.parse(mAluno.getGaleria()), getActivity(), mAluno, ivFoto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return view;
    }

    private View.OnClickListener onClickFoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgAlunoDialog.show(getFragmentManager(), mAluno);
            }
        };
    }

    private List<AlunoAtividade> listAtividades() {
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(getActivity());
        List<AlunoAtividade> listAlunoAtitidades = dao.listar(mAluno);

        String items[] = new AtividadeHelper().atividades();

        int totalFeita = 0;
        int total = 0;
        int feito;
        int naofeito;
        AtividadeDAO atDAO = new AtividadeDAO(getActivity());
        for (int i = 0; i < items.length; i++) {
            feito = 0;
            naofeito = 0;
            for (AlunoAtividade a : listAlunoAtitidades) {
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
            addAtividade(items[i], feito, (feito + naofeito));
        }

        addAtividade("Total", totalFeita, total);
        atDAO.close();
        poncenAtividadesFeitas(totalFeita, total);
        return listAlunoAtitidades;
    }

    private void addAtividade(String atividade, int feitas, int naofeitas) {
        android.widget.TextView tv = new android.widget.TextView(getActivity());
        tv.setText(atividade + ": " + feitas + "/" + naofeitas);
        lnAtividades.addView(tv);
    }

    private void listNotas() {
        NotaDAO dao = new NotaDAO(getActivity());
        List<Nota> notas = dao.listar(mAluno, "ativo");
        dao.close();
        double notaTotal = 0;
        int count = 0;
        for (Nota nota : notas) {
            notaTotal = calculaNota(nota.getNota(), nota.getPontoExtra()) + notaTotal;
            addNotas("Nota: ", calculaNota(nota.getNota(), nota.getPontoExtra()));
            count++;
        }
        media(notaTotal, count);
    }

    private void addNotas(String msg, double notaFinal) {
        android.widget.TextView tv = new android.widget.TextView(getActivity());
        tv.setText(msg + notaFinal);
        lnNotas.addView(tv);
    }

    private double calculaNota(String nota, String pontos) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        if (!nota.equals("") && !nota.equals(null) && !pontos.equals("") && !pontos.equals(null)) {
            return Double.valueOf(nota).doubleValue()+Double.valueOf(pontos);
        } else if (!pontos.equals("") && !pontos.equals(null)) {
            return Double.valueOf(pontos).doubleValue();
        } else if (!nota.equals("") && !nota.equals(null)) {
            return Double.valueOf(nota).doubleValue();
        }
        return 0;
    }

    private void poncenAtividadesFeitas(int totalFeito, int total) {
        if (total > 0 ) {
            int x = (totalFeito * 100) / total;
            android.widget.TextView tv = new android.widget.TextView(getActivity());
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

    private void media(double notaTotal, int count) {
        if (count > 0 ) {
            double media = notaTotal / count;
            BigDecimal arrendodarResultado = new BigDecimal(media).setScale(2, RoundingMode.HALF_EVEN);
            android.widget.TextView tv = new android.widget.TextView(getActivity());
            tv.setText("Media: " + arrendodarResultado);
            if (media >= 7) {
                tv.setTextColor(getActivity().getResources().getColor(R.color.accent));
            } else {
                tv.setTextColor(getActivity().getResources().getColor(R.color.red));
            }
            lnNotas.addView(tv);
        }
    }
}
