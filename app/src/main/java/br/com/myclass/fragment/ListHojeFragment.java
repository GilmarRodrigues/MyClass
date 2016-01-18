package br.com.myclass.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rey.material.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import br.com.myclass.R;
import br.com.myclass.adapter.HojeAdapter;
import br.com.myclass.dao.HorarioDAO;
import br.com.myclass.fragment.dialog.DetalhesHorarioDialog;
import br.com.myclass.model.Horario;
import br.com.myclass.model.ListHorario;

public class ListHojeFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private HojeAdapter mAdapter;
    private List<Horario> mListHorarios;
    private TextView tDiaData;
    private ImageView ivSol;
    private TextView tMsgApDia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_hoje, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_hoje);
        mRecyclerView.setHasFixedSize(true);

        tDiaData = (TextView) view.findViewById(R.id.tv_dia_data);
        tDiaData.setText(dataDeHoje());
        ivSol = (ImageView) view.findViewById(R.id.iv_sol);
        tMsgApDia = (TextView) view.findViewById(R.id.tv_msg_ap_dia);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ListHorario list = (ListHorario) savedInstanceState.getSerializable(ListHorario.KEY);
            mListHorarios = list.mHorarios;
        }

        carregarLista();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListHorario.KEY, new ListHorario(mListHorarios));
    }

    private void carregarLista () {
        mListHorarios = new HorarioDAO(getContext()).listar("ativo");

        List<Horario> aux = new ArrayList<>();
        for (int i=0; i<mListHorarios.size(); i++) {
            if (mListHorarios.get(i).getDia().equals(diaSemana())) {
                aux.add(mListHorarios.get(i));
            }
        }
        mListHorarios = aux;
        ordenarLista();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new HojeAdapter(mListHorarios, getActivity(), onClickListener());
        mRecyclerView.setAdapter(mAdapter);

        showSol();
    }

    private void showSol() {
        if (mListHorarios.size() <= 0) {
            ivSol.setVisibility(View.VISIBLE);
            tMsgApDia.setVisibility(View.VISIBLE);
        } else {
            ivSol.setVisibility(View.INVISIBLE);
            tMsgApDia.setVisibility(View.INVISIBLE);
        }
    }

    private HojeAdapter.OnClickListener onClickListener() {
        return new HojeAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int idx) {
                DetalhesHorarioDialog.show(getFragmentManager(), mListHorarios.get(idx));
            }
        };
    }

    private String diaSemana() {
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        String nome = "";
        int dia = c.get(c.DAY_OF_WEEK);
        switch(dia){
            case Calendar.SUNDAY:
                nome = "Domingo";
                break;
            case Calendar.MONDAY:
                nome = "Segunda";
                break;
            case Calendar.TUESDAY:
                nome = "Terça";
                break;
            case Calendar.WEDNESDAY:
                nome = "Quarta";
                break;
            case Calendar.THURSDAY:
                nome = "Quinta";
                break;
            case Calendar.FRIDAY:
                nome = "Sexta";
                break;
            case Calendar.SATURDAY:
                nome = "Sábado";
                break;
        }
        return nome;
    }

    private String dataDeHoje() {
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
        Locale brasil = new Locale("pt", "BR");
        DateFormat f2 = DateFormat.getDateInstance(DateFormat.FULL, brasil);
        return f2.format(data);
    }

    private void ordenarLista () {
        Collections.sort(mListHorarios, new Comparator<Horario>() {
            DateFormat f = new SimpleDateFormat("HH:mm");

            @Override
            public int compare(Horario o1, Horario o2) {
                try {
                    return f.parse(o1.getHoraInicio()).compareTo(f.parse(o2.getHoraInicio()));
                } catch (java.text.ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        for (int i = 0; mListHorarios.size() > i; i++) {
            mListHorarios.get(i).setId((long) i);
        }
    }


}