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
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.myclass.R;
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.helper.HorarioHelper;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListHorario;

/**
 * Created by gilmar on 04/01/16.
 */
public class EditarHorarioDialog extends BaseDialog implements TimePickerDialog.OnTimeSetListener{
    private Callback callback;
    private Horario mHorario;
    private EditText tHoraInicio;
    private EditText tHoraFim;
    private int hr, mm;
    private int hr2, mm2;
    private boolean hora;
    private HorarioHelper mHelper;

    public static void show(FragmentManager fm, Horario horario, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_horario");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarHorarioDialog frag = new EditarHorarioDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListHorario.KEY, horario);
        frag.setArguments(args);
        frag.show(ft, "editar_horario");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Novo Hórario");
        View view = inflater.inflate(R.layout.dialog_horario, container, false);
        mHelper = new HorarioHelper(getActivity(), view);

        tHoraInicio = (EditText) view.findViewById(R.id.edt_hora_inicio);
        tHoraInicio.setOnClickListener(onClickHoraInicio());
        tHoraInicio.setOnFocusChangeListener(onFocusChangeHoraInicio());

        tHoraFim= (EditText) view.findViewById(R.id.edt_hora_fim);
        tHoraFim.setOnClickListener(onClickHoraFim());
        tHoraFim.setOnFocusChangeListener(onFocusChangeHoraFim());

        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        
        mHorario = (Horario) getArguments().getSerializable(ListHorario.KEY);
        if (mHorario != null) {
            mHelper.colocaHorarioNoFormulario(mHorario);
        }

        try {
            transHoraInicial();
            transHoraFinal();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHorario = mHelper.pegaHorarioDoFormulario();
                String novaHoraInicio = tHoraInicio.getText().toString();
                String novaHoraFim = tHoraFim.getText().toString();
                if (mHorario == null) {
                    mHelper.alert(getFragmentManager(), true);
                    return;
                }
                if (novaHoraInicio == null || novaHoraInicio.trim().length() == 0) {
                    mHelper.alert(getFragmentManager(), true);
                    return;
                }
                if (novaHoraFim == null || novaHoraFim.trim().length() == 0) {
                    mHelper.alert(getFragmentManager(), true);
                    return;
                }
                if (mHelper.ordenarLista()) {
                    mHelper.alertHora(getFragmentManager());
                    return;
                }

                Context context = v.getContext();
                HorarioDAO dao = new HorarioDAO(context);
                mHorario.setHoraInicio(novaHoraInicio);
                mHorario.setHoraFim(novaHoraFim);
                dao.atualizar(mHorario);
                dao.close();
                if (callback != null) {
                    callback.onUpdateHorario(mHorario);
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

    public void setHora() {
        Calendar now = Calendar.getInstance();
        if (hora) {
            now.set(Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH, hr, mm);
        } else {
            now.set(Calendar.YEAR, Calendar.MONTH,Calendar.DAY_OF_MONTH, hr2, mm2);
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                EditarHorarioDialog.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.dismissOnPause(true);
        tpd.setOnCancelListener(EditarHorarioDialog.this);
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
        if (hr == 0) {
            hr = mm = 0;
        } else if (hr2 == 0) {
            hr2 = mm2 = 0;
        }
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

        if(tpd != null) tpd.setOnTimeSetListener(this);
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

    public interface Callback {
        public void onUpdateHorario(Horario horario);
    }
}
