package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.model.Aluno;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 01/01/16.
 */
public class AlunoDialogAdapter extends RecyclerView.Adapter<AlunoDialogAdapter.ViewHolder>{
    private List<Aluno> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private OnClickListener onClickListener;
    private OnClickCheckBox onClickCheckBox;

    public AlunoDialogAdapter(List<Aluno> alunoList, Activity context, OnClickListener onClickListener, OnClickCheckBox onClickCheckBox) {
        this.mList = alunoList;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
        this.onClickCheckBox = onClickCheckBox;
    }

    @Override
    public AlunoDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_aluno, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AlunoDialogAdapter.ViewHolder holder, final int position) {
        holder.tNome.setText(mList.get(position).getNome());
        holder.tSobrenome.setText(mList.get(position).getSobrenome());
        if (mList.get(position).getStatus().equals("ativo")) {
            holder.cbAluno.setChecked(true);
        }else {
            holder.cbAluno.setChecked(false);
        }
        Bitmap bitmap = null;
        int w = holder.ivFoto.getWidth();
        int h = holder.ivFoto.getHeight();
        if(mList.get(position).getFoto() != null) {
            File caminhoArquivo = new File(mList.get(position).getFoto());
            bitmap = ImageUtils.getResizedImage(Uri.fromFile(caminhoArquivo), w, h, false);
        }else if(mList.get(position).getGaleria() != null){
            try {
                bitmap = ImageUtils.getBitmapFromUri(Uri.parse(mList.get(position).getGaleria()), mContext, w, h);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bitmap != null){
            holder.ivFoto.setImageBitmap(bitmap);
        }else{
            holder.ivFoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.aluno));
        }

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
        }

        if (onClickCheckBox != null) {
            holder.cbAluno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCheckBox.onClickCheckBox(v, position);
                }
            });
        }
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
        public RoundedImageView ivFoto;
        public TextView tNome;
        public TextView tSobrenome;
        public CheckBox cbAluno;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFoto = (RoundedImageView) itemView.findViewById(R.id.iv_foto);
            tNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tSobrenome = (TextView) itemView.findViewById(R.id.tv_sobrenome);
            cbAluno = (CheckBox) itemView.findViewById(R.id.cb_aluno);
        }
    }
}
