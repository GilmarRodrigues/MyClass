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
import br.com.myclass.model.Aula;

/**
 * Created by gilmar on 21/12/15.
 */
public class AulaAdapter extends RecyclerView.Adapter<AulaAdapter.ViewHolder> {
    private List<Aula> mListAulas;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private PopupMenuOnClickListener popupmenuOnClickListener;
    private OnClickListener onClickListener;

    public AulaAdapter(List<Aula> mListAulas, Activity mContext, PopupMenuOnClickListener popupmenuOnClickListener, OnClickListener onClickListener) {
        this.mListAulas = mListAulas;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupmenuOnClickListener = popupmenuOnClickListener;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aula, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tConteudo.setText(mListAulas.get(position).getConteudo());
        holder.tData.setText("Data: " + mListAulas.get(position).getData());

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
        return mListAulas != null ? mListAulas.size() : 0;
    }

    public void remove(int position) {
        mListAulas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Aula aula) {
        mListAulas.add(aula);
        notifyItemChanged(position, aula);
        notifyDataSetChanged();
    }
    public interface PopupMenuOnClickListener {
        public void onClickMenuPopup(View view, int idx);

    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tConteudo;
        public TextView tData;
        public ImageView ivPopupmenu;

        public ViewHolder(View itemView) {
            super(itemView);
            tConteudo = (TextView) itemView.findViewById(R.id.tv_conteudo);
            tData = (TextView) itemView.findViewById(R.id.tv_data);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
        }

    }

}
