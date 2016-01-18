package br.com.myclass.fragment.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.helper.TurmaHelper;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Turma;

public class EditarTurmaDialog extends BaseDialog implements DatePickerDialog.OnDateSetListener {
    private Callback callback;
    private Turma turma;
    private EditText tDataInicial;
    private EditText tDataFinal;
    private String novaDataFinal;
    private String novaDataInical;
    private boolean date;
    private TurmaHelper mHelper;
    private int year, month, day;
    private int year2, month2, day2;
    private LinearLayout lnHorario;
    private TextView tAddHorario;
    private List<Horario> mHorarioList;
    private TextView tExcluirHorario;

    public static void show(FragmentManager fm, Turma turma, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_turma");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarTurmaDialog frag = new EditarTurmaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "editar_turma");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.turma_atualizar);
        View view = inflater.inflate(R.layout.dialog_turma, container, false);

        mHelper = new TurmaHelper(getActivity(), view);
        mHorarioList = new ArrayList<>();


        view.findViewById(R.id.btn_editar).setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        tDataInicial = (EditText) view.findViewById(R.id.edt_data_inicial);
        tDataInicial.setOnClickListener(onClickDataInical());
        tDataInicial.setOnFocusChangeListener(onFocusChangeDataInicial());
        tDataFinal = (EditText) view.findViewById(R.id.edt_data_fim);
        tDataFinal.setOnClickListener(onClickDataFinal());
        tDataFinal.setOnFocusChangeListener(onFocusChangeDataFinal());
        lnHorario = (LinearLayout) view.findViewById(R.id.ln_horario);
        tAddHorario = (TextView) view.findViewById(R.id.tv_horario);
        tAddHorario.setOnClickListener(onClickHoario());
        tExcluirHorario = (TextView) view.findViewById(R.id.tv_horario_excluir);
        tExcluirHorario.setOnClickListener(onClickExCluirhorario());

        this.turma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        if (turma != null) {
            mHelper.colocaTurmaNoFormulario(turma);
            tDataInicial.setText(turma.getDataInicial());
            tDataFinal.setText(turma.getDataFinal());
            addHorario();
        }

        try {
            transDateInicial();
            transDateFinal();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private View.OnClickListener onClickExCluirhorario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExcluirHorarioTurmaDialog.show(getFragmentManager(), turma, new ExcluirHorarioTurmaDialog.Callback() {
                    @Override
                    public void onDeleteHorario(List<Horario> horarioList) {
                        lnHorario.removeAllViews();
                        addHorario();
                        Snackbar.make(getView(), "Horários excluidos com sucesso!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    private View.OnClickListener onClickHoario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdicionarHorarioTurmaDialog.show(getFragmentManager(), new AdicionarHorarioTurmaDialog.Callback() {
                    @Override
                    public void onAddHorario(Horario horario) {
                        HorarioDAO horarioDAO = new HorarioDAO(getActivity());
                        horario.setTurmaId(turma.getId());
                        horario.setStatus("ativo");
                        horarioDAO.inserir(horario);
                        horarioDAO.close();
                        lnHorario.removeAllViews();
                        addHorario();

                    }
                });
            }
        };
    }


    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novaDisciplina = mHelper.tDisciplina.getText().toString();
                String novoCurso = mHelper.tCurso.getText().toString();
                novaDataInical = tDataInicial.getText().toString();
                novaDataFinal = tDataFinal.getText().toString();
                if (novaDisciplina == null || novaDisciplina.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if (novoCurso == null || novoCurso.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if (novaDataInical == null || novaDataInical.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if (novaDataFinal == null || novaDataFinal.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }

                if (mHelper.ordenarLista()) {
                    mHelper.alertHora(getFragmentManager());
                    return;
                }
                Context context = view.getContext();
                turma = mHelper.pegaTurmaDoFormulario(novaDisciplina, novoCurso);
                turma.setDataInicial(novaDataInical);
                turma.setDataFinal(novaDataFinal);
                TurmaDAO dao = new TurmaDAO(context);
                dao.atualizar(turma);

                if (callback != null) {
                    callback.onTurmaUpdate(turma);
                }
                dao.close();
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

    private View.OnClickListener onClickDataInical() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = true;
                setData();
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeDataInicial() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    date = true;
                    setData();
                }
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeDataFinal() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    date = false;
                    setData();
                }
            }
        };
    }

    private View.OnClickListener onClickDataFinal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = false;
                setData();
            }
        };
    }

    public void setData() {
        initData();
        Calendar now = Calendar.getInstance();
        if (date) {
            now.set(year, month, day);
        } else {
            now.set(year2, month2, day2);
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                EditarTurmaDialog.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnCancelListener(EditarTurmaDialog.this);
        dpd.setAccentColor(Color.parseColor("#4CAF50"));
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void initData() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else if (year2 == 0) {
            Calendar c = Calendar.getInstance();
            year2 = c.get(Calendar.YEAR);
            month2 = c.get(Calendar.MONTH);
            day2 = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (year == 0) {
            year = month = day = 0;
        } else if (year2 == 0) {
            year2 = month2 = day2 = 0;
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int yearAll, int monthOfYear, int dayOfMonth) {
        try {
            String data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearAll;
            setCamposDadas(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    // Interface para retornar o resultado
    public static interface Callback {
        public void onTurmaUpdate(Turma turma);
    }

    private void setCamposDadas(String data) throws ParseException {
        if (date) {
            tDataInicial.setText(data);
            novaDataInical = tDataInicial.getText().toString();
            transDateInicial();
        } else {
            tDataFinal.setText(data);
            novaDataFinal = tDataFinal.getText().toString();
            transDateFinal();
        }
    }

    // Converte String para Data
    private void transDateInicial() throws ParseException {
        String dataNull = tDataInicial.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String data = tDataInicial.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }

    }

    // Converte String para Data
    private void transDateFinal() throws ParseException {
        String dataNull = tDataFinal.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
            Calendar c = Calendar.getInstance();
            year2 = c.get(Calendar.YEAR);
            month2 = c.get(Calendar.MONTH);
            day2 = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String data = tDataFinal.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            day2 = c.get(Calendar.DAY_OF_MONTH);
            month2 = c.get(Calendar.MONTH);
            year2 = c.get(Calendar.YEAR);
        }
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