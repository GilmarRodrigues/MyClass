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
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.dao.AulaDAO;
import br.com.myclass.dao.AulaProvaDAO;
import br.com.myclass.dao.ProvaDAO;
import br.com.myclass.helper.ProvaHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.Aula;
import br.com.myclass.model.AulaProva;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 28/12/15.
 */
public class AdicionarProvaDialog extends BaseDialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Callback callback;
    private EditText tData;
    private EditText tHora;
    private TextView tContProva;
    private Turma mTurma;
    private int year, month, day;
    private int hr, mm;
    private List<Aula> mListAulas;
    private LinearLayout lnConteudoProva;
    private ProvaHelper mHelper;

    public static void show(FragmentManager fm, Turma turma, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("adicionar_prova");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        AdicionarProvaDialog frag = new AdicionarProvaDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListTurma.KEY, turma);
        frag.setArguments(args);
        frag.show(ft, "adicionar_prova");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Nova Avaliação");
        View view = inflater.inflate(R.layout.dialog_prova, container, false);
        lnConteudoProva = (LinearLayout) view.findViewById(R.id.ln_conteudo_prova);
        mHelper = new ProvaHelper(getActivity(), view);

        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setText("Salvar");
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());

        tContProva = (TextView) view.findViewById(R.id.tv_conteudo_prova);
        tContProva.setOnClickListener(onClickConteudoProva());
        tData = (EditText) view.findViewById(R.id.edt_data);
        tData.setOnClickListener(onClickData());
        tData.setOnFocusChangeListener(onFocusChangeData());
        tHora = (EditText) view.findViewById(R.id.edt_hora);
        tHora.setOnClickListener(onClickHora());
        tHora.setOnFocusChangeListener(onFocusChangeHora());
        mTurma = (Turma) getArguments().getSerializable(ListTurma.KEY);
        return view;
    }

    private View.OnClickListener onClickConteudoProva() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAddEdAulaProvaDialog.show(getFragmentManager(), mTurma, mListAulas, new ListAddEdAulaProvaDialog.Callback() {
                    @Override
                    public void onConteudoProvaAdd(List<Aula> aulaList) {
                        mListAulas = aulaList;
                        lnConteudoProva.removeAllViews();
                        for (int i = 0; i < aulaList.size(); i++) {
                            if (aulaList.get(i).getStatus().equals("ativo")) {
                                TextView tv = new TextView(getActivity());
                                tv.setText(aulaList.get(i).getConteudo().toString());
                                lnConteudoProva.addView(tv);
                            }
                        }

                    }
                });
            }
        };
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novaDescricao = mHelper.gettDescricao().getText().toString();
                String novaData = tData.getText().toString();
                if (novaDescricao == null || novaDescricao.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if (novaData == null || novaData.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                Context context = v.getContext();
                ProvaDAO dao = new ProvaDAO(context);
                Prova prova = mHelper.pegaProvaDoFormulario(novaDescricao);
                prova.setStatus("ativo");
                prova.setData(novaData);
                prova.setHora(tHora.getText().toString());
                prova.setTurmaId(mTurma.getId());
                Long id = dao.inserir(prova);
                dao.close();

                if (mListAulas != null) {
                    AulaProvaDAO aulaProvaDAO = new AulaProvaDAO(context);
                    for (Aula aula: mListAulas) {
                        if (aula.getStatus().equals("ativo")) {
                            AulaProva ap = new AulaProva();
                            ap.setStatus("ativo");
                            ap.setAulaId(aula.getId());
                            ap.setProvaId(id);
                            aulaProvaDAO.inserir(ap);
                        }
                    }
                    aulaProvaDAO.close();
                }

                if (callback != null) {
                    callback.onProvaAdd(prova);
                }
                dismiss();
            }
        };
    }

    private List<Aula> listAulas() {
        AulaDAO dao = new AulaDAO(getActivity());
        List<Aula> list = null;
        if (mTurma != null) {
            list = dao.listar(mTurma);
        }
        dao.close();
        return list;
    }

    private List<Aluno> listAlunos() {
        AlunoDAO dao = new AlunoDAO(getActivity());
        List<Aluno> listar = null;
        if (mTurma != null) {
            listar = dao.listar(mTurma);
        }
        dao.close();
        return listar;
    }

    private View.OnClickListener onClickCancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

    private View.OnFocusChangeListener onFocusChangeHora() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setHora();
                }
            }
        };
    }

    private View.OnClickListener onClickHora() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHora();
            }
        };
    }

    public void setData() {
        initData();
        Calendar now = Calendar.getInstance();
        now.set(year, month, day);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AdicionarProvaDialog.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnCancelListener(AdicionarProvaDialog.this);
        dpd.setAccentColor(Color.parseColor("#4CAF50"));
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void initData() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void setHora() {
        initHora();
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hr, mm);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AdicionarProvaDialog.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.dismissOnPause(true);
        tpd.setOnCancelListener(AdicionarProvaDialog.this);
        tpd.setAccentColor(Color.parseColor("#4CAF50"));
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    private void initHora() {
        if (hr == 0) {
            Calendar c = Calendar.getInstance();
            hr = c.get(Calendar.HOUR_OF_DAY);
            mm = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int yearAll, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = monthOfYear;
        year = yearAll;
        String data = day + "/" + (month + 1) + "/" + yearAll;
        tData.setText(data);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        hr = hourOfDay;
        mm = minute;
        String hora = hourOfDay + ":" + minute;
        tHora.setText(hora);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (year == 0) {
            year = month = day = 0;
            tData.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    // Interface para retornar o resultado
    public static interface Callback {
        public void onProvaAdd(Prova prova);
    }


}
