package br.com.myclass.adapter;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.Collections;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 31/07/15.
 */
public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.ViewHolder> {
    ColorGenerator generator = ColorGenerator.MATERIAL;
    private List<Turma> mListTurmas;
    private OnClickListener onClickListener;
    private LayoutInflater mLayoutInflater;
    private FragmentActivity mContext;
    private PopupMenuOnClickListener popupmenuOnClickListener;
    private String letter;

    public TurmaAdapter(List<Turma> turmas, FragmentActivity context, PopupMenuOnClickListener popupmenuOnClickListener, OnClickListener onClickListener) {
        this.mListTurmas = turmas;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.popupmenuOnClickListener = popupmenuOnClickListener;
        this.onClickListener = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_turma, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvDescricao.setText(mListTurmas.get(position).getDisciplina());
        holder.tvCurso.setText(mListTurmas.get(position).getCurso());

        letter = String.valueOf(mListTurmas.get(position).getDisciplina().charAt(0));

        // Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());

        holder.letter.setImageDrawable(drawable);

        // Click PopupMenu
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
        return mListTurmas != null ? mListTurmas.size() : 0;
    }


    public void remove(int position) {
        mListTurmas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Turma turma) {
        mListTurmas.add(turma);
        notifyItemChanged(position, turma);
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mListTurmas, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mListTurmas, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
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
        public TextView tvDescricao;
        public TextView tvCurso;
        public ImageView ivPopupmenu;
        public ImageView letter;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDescricao = (TextView) itemView.findViewById(R.id.tv_disciplina);
            tvCurso = (TextView) itemView.findViewById(R.id.tv_curso);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
            letter = (ImageView) itemView.findViewById(R.id.gmailitem_letter);

        }

    }
}
