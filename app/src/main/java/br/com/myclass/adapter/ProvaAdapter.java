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
import br.com.myclass.model.Prova;

/**
 * Created by gilmar on 28/12/15.
 */
public class ProvaAdapter extends RecyclerView.Adapter<ProvaAdapter.ViewHolder>{
    private List<Prova> mListProvas;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private PopupMenuOnClickListener popupmenuOnClickListener;
    private OnClickListener onClickListener;

    public ProvaAdapter(List<Prova> mListProvas, Activity mContext, PopupMenuOnClickListener popupmenuOnClickListener, OnClickListener onClickListener) {
        this.mListProvas = mListProvas;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupmenuOnClickListener = popupmenuOnClickListener;
        this.onClickListener = onClickListener;
    }
    
    @Override
    public ProvaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prova, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProvaAdapter.ViewHolder holder, final int position) {
        holder.tDescricao.setText(mListProvas.get(position).getDescricao());
        holder.tDataHora.setText("Data: " + mListProvas.get(position).getData() +" Hora: " + mListProvas.get(position).getHora());

        if (popupmenuOnClickListener != null) {
            holder.ivPopupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupmenuOnClickListener.onClickMenuPopup(v, position);
                }
            });
        }

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mListProvas != null ? mListProvas.size() : 0;
    }

    public void remove(int position) {
        mListProvas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Prova prova) {
        mListProvas.add(prova);
        notifyItemChanged(position, prova);
        notifyDataSetChanged();
    }
    public interface PopupMenuOnClickListener {
        public void onClickMenuPopup(View view, int idx);

    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tDescricao;
        public TextView tDataHora;
        public ImageView ivPopupmenu;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tDescricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            tDataHora = (TextView) itemView.findViewById(R.id.tv_data_hora);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
        }
    }
}
