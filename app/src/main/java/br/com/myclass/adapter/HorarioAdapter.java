package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.TurmaDAO;
import br.com.myclass.model.Horario;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 04/01/16.
 */
public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {
    private List<Horario> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private OnClickListener onClickListener;
    private PopupMenuOnClickListener popupMenuOnClickListener;

    public HorarioAdapter(List<Horario> mList, Activity mContext, OnClickListener onClickListener, PopupMenuOnClickListener popupMenuOnClickListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
        this.popupMenuOnClickListener = popupMenuOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_horario, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tDia.setText(mList.get(position).getDia() + ",  " + mList.get(position).getHoraInicio() + " às " +mList.get(position).getHoraFim());
        holder.tCurso.setText("Curso/Série: " + getTurma(position).getCurso());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
        }

        if (popupMenuOnClickListener != null) {
            holder.ivPopupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenuOnClickListener.onClickMenuPopup(v, position);
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

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Horario horario) {
        mList.add(horario);
        notifyItemChanged(position, horario);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public interface PopupMenuOnClickListener {
        public void onClickMenuPopup(View view, int idx);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tDia;
        public TextView tCurso;
        public ImageView ivPopupmenu;

        public ViewHolder(View itemView) {
            super(itemView);
            tDia = (TextView) itemView.findViewById(R.id.tv_dia);
            tCurso = (TextView) itemView.findViewById(R.id.tv_curso);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
        }
    }
}
