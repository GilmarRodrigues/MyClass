package br.com.myclass.fragment.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.myclass.R;
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.helper.AulaHelper;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListAula;

/**
 * Created by gilmar on 22/12/15.
 */
public class EditarAulaDialog extends BaseDialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Callback callback;
    private EditText tData;
    private EditText tHoraInicio;
    private EditText tHoraFim;
    private AulaHelper mHelper;
    private Aula mAula;
    private int year, month, day;
    private int hr, mm;
    private int hr2, mm2;
    private boolean hora;
    private String novaData;

    public static void show(FragmentManager fm, Aula aula, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_aula");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarAulaDialog frag = new EditarAulaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListAula.KEY, aula);
        frag.setArguments(args);
        frag.show(ft, "editar_aula");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Atualizar Aula");
        View view = inflater.inflate(R.layout.dialog_aula, container, false);
        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        mHelper = new AulaHelper(getActivity(), view);
        tData = (EditText) view.findViewById(R.id.edt_data);
        tData.setOnClickListener(onClickData());
        tData.setOnFocusChangeListener(onFocusChangeData());
        tHoraInicio = (EditText) view.findViewById(R.id.edt_hora_inicio);
        tHoraInicio.setOnClickListener(onClickHoraInicio());
        tHoraInicio.setOnFocusChangeListener(onFocusChangeHoraInicio());
        tHoraFim= (EditText) view.findViewById(R.id.edt_hora_fim);
        tHoraFim.setOnClickListener(onClickHoraFim());
        tHoraFim.setOnFocusChangeListener(onFocusChangeHoraFim());

        this.mAula = (Aula) getArguments().getSerializable(ListAula.KEY);
        if (mAula != null) {
            mHelper.colocaAulaNoFormulario(mAula);
            tData.setText(mAula.getData());
            tHoraInicio.setText(mAula.getHoraInicio());
            tHoraFim.setText(mAula.getHoraFim());
        }
        try {
            transDate();
            transHoraInicial();
            transHoraFinal();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoConteudo = mHelper.tConteudo.getText().toString();
                if (novoConteudo == null || novoConteudo.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                novaData = tData.getText().toString();
                if (novaData == null || novaData.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if(tHoraInicio.getText().toString() != null && tHoraInicio.getText().toString().trim().length() != 0 &&
                        tHoraInicio.getText().toString() != null && tHoraFim.getText().toString().trim().length() != 0) {
                    if (mHelper.ordenarLista()) {
                        mHelper.alertHora(getFragmentManager());
                        return;
                    }
                }

                Context context = getView().getContext();
                mAula = mHelper.pegaAuladoFormulario(novoConteudo);
                mAula.setTurmaId(mAula.getTurmaId());
                mAula.setStatus("ativo");
                mAula.setData(novaData);
                mAula.setHoraInicio(tHoraInicio.getText().toString());
                mAula.setHoraFim(tHoraFim.getText().toString());
                AulaDAO dao = new AulaDAO(context);
                dao.atualizar(mAula);
                if (callback != null) {
                    callback.onAulaAdd(mAula);
                }
                dao.close();
                dismiss();
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeHoraInicio() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hora = true;
                    initHora();
                    setHora();
                }
            }
        };
    }

    private View.OnClickListener onClickHoraInicio() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = true;
                initHora();
                setHora();
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeHoraFim() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hora = false;
                    initHora();
                    setHora();
                }
            }
        };
    }

    private View.OnClickListener onClickHoraFim() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = false;
                initHora();
                setHora();
            }
        };
    }

    private View.OnClickListener onClickData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeData() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setData();
                }
            }
        };
    }

    public void setData() {
        initData();
        Calendar now = Calendar.getInstance();
        now.set(year,month,day);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                EditarAulaDialog.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.dismissOnPause(true);
        dpd.setOnCancelListener(EditarAulaDialog.this);
        dpd.setAccentColor(Color.parseColor("#4CAF50"));
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void  initData(){
        if(year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void setHora() {
        Calendar now = Calendar.getInstance();
        if (hora) {
            now.set(Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH, hr, mm);
        } else {
            now.set(Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH, hr2, mm2);
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                EditarAulaDialog.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.dismissOnPause(true);
        tpd.setOnCancelListener(EditarAulaDialog.this);
        tpd.setAccentColor(Color.parseColor("#4CAF50"));
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    private void  initHora(){
        if(hr == 0 && hr2 == 0) {
            Calendar c = Calendar.getInstance();
            hr = c.get(Calendar.HOUR_OF_DAY);
            mm = c.get(Calendar.MINUTE);
            hr2 = c.get(Calendar.HOUR_OF_DAY);
            mm2 = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (year == 0) {
            year = month = day = 0;
            tData.setText("");
        } else if (hr == 0) {
            hr = mm = 0;
        } else if (hr2 == 0) {
            hr2 = mm2 = 0;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int yearAll, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = monthOfYear;
        year = yearAll;
        String data = dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearAll;
        tData.setText(data);
        novaData = data;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        try {
            String hora = hourOfDay + ":" + minute;
            setCamposHoras(hora);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setCamposHoras(String hora) throws ParseException {
        if (this.hora) {
            tHoraInicio.setText(hora);
            transHoraInicial();
        } else {
            tHoraFim.setText(hora);
            transHoraFinal();
        }
    }

    // Interface para retornar o resultado
    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    public static interface Callback {
        public void onAulaAdd(Aula aula);
    }

    // Converte String para Hora
    private void transHoraInicial() throws ParseException {
        String dataNull = tHoraInicio.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
            Calendar c = Calendar.getInstance();
            hr = c.get(Calendar.HOUR_OF_DAY);
            mm = c.get(Calendar.MINUTE);
        } else {
            String data = tHoraInicio.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            hr = c.get(Calendar.HOUR_OF_DAY);
            mm = c.get(Calendar.MINUTE);
        }

    }

    // Converte String para Hora
    private void transHoraFinal() throws ParseException {
        String dataNull = tHoraFim.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
            Calendar c = Calendar.getInstance();
            hr2 = c.get(Calendar.HOUR_OF_DAY);
            mm2 = c.get(Calendar.MINUTE);
        } else {
            String data = tHoraFim.getText().toString();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            hr2 = c.get(Calendar.HOUR_OF_DAY);
            mm2 = c.get(Calendar.MINUTE);
        }
    }

    // Converte String para Data
    private void transDate() throws ParseException {
        String dataNull = tData.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
        } else {
            String data = mAula.getData();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }

    }
}
