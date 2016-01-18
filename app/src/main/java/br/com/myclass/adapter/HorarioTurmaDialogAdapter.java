package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.model.Horario;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 06/01/16.
 */
public class HorarioTurmaDialogAdapter extends RecyclerView.Adapter<HorarioTurmaDialogAdapter.ViewHolder> {
    private List<Horario> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private OnClickListener onClickListener;
    private OnClickCheckBox onClickCheckBox;

    public HorarioTurmaDialogAdapter(List<Horario> mList, Activity mContext, OnClickListener onClickListener, OnClickCheckBox onClickCheckBox) {
        this.mList = mList;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
        this.onClickCheckBox = onClickCheckBox;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_horario, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tDia.setText(mList.get(position).getDia());
        holder.tHora.setText(mList.get(position).getHoraInicio() + " às " +mList.get(position).getHoraFim());
        mList.get(position).setStatus("ativo");


        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
        }

        if (onClickCheckBox != null) {
            holder.cbHorario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCheckBox.onClickCheckBox(v, position);
                }
            });
        }

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

    public interface OnClickCheckBox {
        public void onClickCheckBox(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tDia;
        public TextView tHora;
        public CheckBox cbHorario;

        public ViewHolder(View itemView) {
            super(itemView);
            tDia = (TextView) itemView.findViewById(R.id.tv_dia);
            tHora = (TextView) itemView.findViewById(R.id.tv_hora);
            cbHorario = (CheckBox) itemView.findViewById(R.id.cb_turma_horario);
        }
    }
}
