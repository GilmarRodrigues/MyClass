package br.com.myclass.helper;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;

import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.model.Horario;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 04/01/16.
 */
public class HorarioHelper {
    private Spinner spnDia;
    private Spinner spnTurma;
    private EditText tHoraInicio;
    private EditText tDescricao;
    private EditText tHoraFim;
    private String[] itensDia;
    private String[] itensTurma;
    private Activity mContext;
    private Horario mHorario;
    private String novoDia;
    private Long novaTurma;
    private List<Turma> turmaList;

    public HorarioHelper(Activity context, View view) {
        this.mHorario = new Horario();
        this.mContext = context;

        tHoraInicio = (EditText) view.findViewById(R.id.edt_hora_inicio);
        tHoraFim= (EditText) view.findViewById(R.id.edt_hora_fim);
        tDescricao= (EditText) view.findViewById(R.id.edt_descricao);

        spnDia = (Spinner) view.findViewById(R.id.spinner_dia);
        spnTurma = (Spinner) view.findViewById(R.id.spinner_turma);
        diaDaSemana();
        turma();
        ArrayAdapter<String> diaAdapter = new ArrayAdapter<>(context, R.layout.row_spn, itensDia);
        diaAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spnDia.setAdapter(diaAdapter);
        spnDia.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (position > 0) {
                    novoDia = itensDia[position].toString();
                } else {
                    novoDia = null;
                }
            }
        });

        ArrayAdapter<String> turmaAdapter = new ArrayAdapter<>(context, R.layout.row_spn, itensTurma);
        turmaAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spnTurma.setAdapter(turmaAdapter);
        spnTurma.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (position > 0) {
                    novaTurma = turmaList.get(position-1).getId();
                } else {
                    novaTurma = null;
                }
            }
        });

    }

    public Horario pegaHorarioDoFormulario() {
        if (novoDia != null && novaTurma != null) {
            mHorario.setDia(novoDia);
            mHorario.setTurmaId(novaTurma);
        } else {
            return null;
        }
        mHorario.setDescricao(tDescricao.getText().toString());
        return mHorario;
    }

    public Horario pegaHorarioDoFormularioTurmaNull() {
        if (novoDia != null) {
            mHorario.setDia(novoDia);
        } else {
            return null;
        }
        mHorario.setDescricao(tDescricao.getText().toString());
        return mHorario;
    }

    public Horario colocaHorarioNoFormulario(Horario horario) {
        mHorario = horario;
        spnDia.setSelection(getDia(horario));
        spnTurma.setSelection(getTurma(horario));
        tHoraInicio.setText(horario.getHoraInicio());
        tHoraFim.setText(horario.getHoraFim());
        tDescricao.setText(horario.getDescricao());
        return mHorario;

    }

    public void offCampos() {
        getSpnDia().setEnabled(false);
        spnTurma.setEnabled(false);
        tHoraInicio.setEnabled(false);
        tHoraInicio.setTextColor(mContext.getResources().getColor(R.color.black));
        tHoraFim.setEnabled(false);
        tHoraFim.setTextColor(mContext.getResources().getColor(R.color.black));
        tDescricao.setEnabled(false);
        tDescricao.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    private int getDia(Horario horario) {
        for (int i = 0; i < itensDia.length; i++){
            if (horario.getDia().equals(itensDia[i])){
                return i;
            }
        }
        return 0;
    }

    private int getTurma(Horario horario) {
        if (horario.getTurmaId() != 0) {
            for (int i = 0; i < itensTurma.length; i++){
                String curso = new TurmaDAO(mContext).buscarPorId(horario.getTurmaId()).getCurso();
                if (curso.equals(itensTurma[i])){
                    return i;
                }
            }
        }
        return 0;
    }

    private void diaDaSemana() {
        itensDia = new String[8];
        itensDia[0] = "Selecione um item";
        itensDia[1] = "Domingo";
        itensDia[2] = "Segunda";
        itensDia[3] = "Terça";
        itensDia[4] = "Quarta";
        itensDia[5] = "Quinta";
        itensDia[6] = "Sexta";
        itensDia[7] = "Sábado";
    }

    private void turma() {
        TurmaDAO dao = new TurmaDAO(mContext);
        turmaList = dao.listarAtivas();
        dao.close();

        itensTurma = new String[turmaList.size()+1];
        itensTurma[0] = "Selecione um item";
        for (int i=0; i < turmaList.size(); i++) {
            itensTurma[i+1]  = turmaList.get(i).getCurso();
        }
    }

    public boolean ordenarLista() {
        List<String> auxList = new ArrayList<>();
        auxList.add(tHoraInicio.getText().toString());
        auxList.add(tHoraFim.getText().toString());

        Collections.sort(auxList, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("HH:mm");

            @Override
            public int compare(String h1, String h2) {
                try {
                    return f.parse(h1).compareTo(f.parse(h2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        if (tHoraInicio.getText().toString().equals(auxList.get(0))) {
            return false;
        } else {
            return true;
        }

    }

    public void alert(FragmentManager fragmentManager, boolean horarioComTurma) {
        String msg = "";
        setError();
        if (horarioComTurma) {
            msg = "Você deve preencher os campos Dia da semana, Classe, Hora de início da aula e Hora encerramento da aula.";
        } else {
            msg = "Você deve preencher os campos Dia da semana, Hora de início da aula e Hora encerramento da aula.";
        }
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message(msg)
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    public void alertHora(FragmentManager fragmentManager) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight);
        ((SimpleDialog.Builder) builder).message("Campo Hora de início da aula tem que ser menor que Hora encerramento da aula.")
                .title("Atenção!")
                .positiveAction("OK");
        com.rey.material.app.DialogFragment fragment = com.rey.material.app.DialogFragment.newInstance(builder);
        fragment.show(fragmentManager, null);
    }

    private void setError(){
        tHoraFim.setError("Informe hora encerramento da aula");
        tHoraInicio.setError("Informe hora de início da aula");
    }

    public Spinner getSpnDia() {
        return spnDia;
    }
}
