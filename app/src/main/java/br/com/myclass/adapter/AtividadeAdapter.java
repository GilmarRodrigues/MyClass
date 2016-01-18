package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.model.Atividade;

/**
 * Created by gilmar on 23/12/15.
 */
public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {
    private List<Atividade> mListAtividades;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private PopupMenuOnClickListener popupmenuOnClickListener;
    private OnClickListener onClickListener;

    public AtividadeAdapter(List<Atividade> listAtividades, Activity context, PopupMenuOnClickListener popupmenuOnClickListener, OnClickListener onClickListener) {
        this.mListAtividades = listAtividades;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupmenuOnClickListener = popupmenuOnClickListener;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_atividade, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tTipo.setText(mListAtividades.get(position).getTipo());
        holder.tDescricao.setText("Obs.: " + mListAtividades.get(position).getDescricao());

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
        return mListAtividades != null ? mListAtividades.size() : 0;
    }

    public void remove(int position) {
        mListAtividades.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Atividade atividade) {
        mListAtividades.add(atividade);
        notifyItemChanged(position, atividade);
        notifyDataSetChanged();
    }

    public interface PopupMenuOnClickListener {
        public void onClickMenuPopup(View view, int idx);

    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public void animationIn (ViewHolder holder) {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        holder.itemView.setAnimation(animation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tTipo;
        public TextView tDescricao;
        public ImageView ivPopupmenu;

        public ViewHolder(View itemView) {
            super(itemView);
            tTipo = (TextView) itemView.findViewById(R.id.tv_tipo);
            tDescricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
        }
    }
}
