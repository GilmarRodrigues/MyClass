package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class HojeAdapter extends RecyclerView.Adapter<HojeAdapter.ViewHolder> {
    private List<Horario> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private OnClickListener onClickListener;
    private List<String> auxList;
    private String horaAtual;

    public HojeAdapter(List<Horario> mList, Activity mContext, OnClickListener onClickListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hoje, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tDia.setText((position+1)+"° aula " + getTurma(position).getCurso());
        holder.tCurso.setText("das: " + mList.get(position).getHoraInicio() + " às: " +mList.get(position).getHoraFim());
        try {
            holder.tEstado.setText(getEstado(mList.get(position).getHoraInicio(), mList.get(position).getHoraFim(), holder));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ordenarLista(mList.get(position).getHoraInicio(), mList.get(position).getHoraFim());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
        }

    }

    private String getEstado(String horaInicio, String horaFim, ViewHolder holder) throws ParseException {
        ordenarLista(horaInicio, horaFim);
        if (horaAtual == auxList.get(0)) {
            return "próxima";
        } else if (horaAtual == auxList.get(1)) {
            holder.tEstado.setTextColor(mContext.getResources().getColor(R.color.accent));
            return "em andamento";
        } else if (horaAtual == auxList.get(2)) {
            holder.tEstado.setTextColor(mContext.getResources().getColor(R.color.red));
            return "encerrada";
        }

        return "";
    }

    private void ordenarLista (String horaInicio, String horaFim) {
        horaAtual();
        auxList = new ArrayList<>();
        auxList.add(horaInicio);
        auxList.add(horaFim);
        auxList.add(horaAtual);

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

    }

    private void horaAtual(){
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);
        horaAtual = hora+":"+minuto;
    }

    private Turma getTurma(int position) {
        TurmaDAO dao = new TurmaDAO(mContext);
        Turma turma = new Turma();
        if (mList.get(position).getTurmaId() != 0) {
            turma = dao.buscarPorId(mList.get(position).getTurmaId());
        }
        return turma;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tDia;
        public TextView tCurso;
        private TextView tEstado;

        public ViewHolder(View itemView) {
            super(itemView);
            tDia = (TextView) itemView.findViewById(R.id.tv_dia);
            tCurso = (TextView) itemView.findViewById(R.id.tv_curso);
            tEstado = (TextView) itemView.findViewById(R.id.tv_estado);
        }
    }
}
