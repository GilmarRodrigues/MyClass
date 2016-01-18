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
import br.com.myclass.model.Aula;

/**
 * Created by gilmar on 28/12/15.
 */
public class AulaDialogAdapter extends RecyclerView.Adapter<AulaDialogAdapter.ViewHolder> {
    private List<Aula> mListAulas;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private OnClickCheckBox onClickCheckBox;
    private OnClickListener onClickListener;

    public AulaDialogAdapter(List<Aula> mListAulas, Activity mContext, OnClickCheckBox onClickCheckBox, OnClickListener onClickListener) {
        this.mListAulas = mListAulas;
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickCheckBox = onClickCheckBox;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_aula, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tConteudo.setText(mListAulas.get(position).getConteudo());
        holder.tData.setText(mListAulas.get(position).getData());

        if (mListAulas.get(position).getStatus().equals("ativo")) {
            holder.cbAula.setChecked(true);
        }else {
            holder.cbAula.setChecked(false);
        }

        if (onClickCheckBox != null) {
            holder.cbAula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCheckBox.OnClickCheckBox(v, position);
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

    public interface OnClickCheckBox {
        public void OnClickCheckBox(View view, int idx);

    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tConteudo;
        public TextView tData;
        public CheckBox cbAula;

        public ViewHolder(View itemView) {
            super(itemView);
            tConteudo = (TextView) itemView.findViewById(R.id.tv_conteudo);
            tData = (TextView) itemView.findViewById(R.id.tv_data);
            cbAula = (CheckBox) itemView.findViewById(R.id.cb_aula);
        }

    }

}
