package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 27/12/15.
 */
public class AlunoAtividadeDialogAdapter extends RecyclerView.Adapter<AlunoAtividadeDialogAdapter.ViewHolder> {
    private List<AlunoAtividade> mList;
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private OnClickListener onClickListener;
    private OnClickCheckBox onClickCheckBox;

    public AlunoAtividadeDialogAdapter(List<AlunoAtividade> mList, Activity context, OnClickListener onClickListener,
                                       OnClickCheckBox onClickCheckBox) {
        this.mList = mList;
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
        this.onClickCheckBox = onClickCheckBox;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dialog_aluno_atividade, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Aluno aluno = getAluno(position);
        holder.tNome.setText(aluno.getNome());
        holder.tSobrenome.setText(aluno.getSobrenome());
        Drawable img;
        if (mList.get(position).getStatus().equals("feito")) {
            img = mContext.getResources().getDrawable(R.drawable.ic_emoticon_happy);
            holder.cbAtividade.setChecked(true);
        }else {
            img = mContext.getResources().getDrawable(R.drawable.ic_emoticon_sad);
            holder.cbAtividade.setChecked(false);
        }
        holder.cbAtividade.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        Bitmap bitmap = null;
        int w = holder.ivFoto.getWidth();
        int h = holder.ivFoto.getHeight();
        if(aluno.getFoto() != null) {
            File caminhoArquivo = new File(aluno.getFoto());
            bitmap = ImageUtils.getResizedImage(Uri.fromFile(caminhoArquivo), w, h, false);
        }else if(aluno.getGaleria() != null){
            try {
                bitmap = ImageUtils.getBitmapFromUri(Uri.parse(aluno.getGaleria()), mContext, w, h);
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
            holder.cbAtividade.setOnClickListener(new View.OnClickListener() {
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

    private Aluno getAluno(int position){
        Long id = mList.get(position).getAlunoId();
        AlunoDAO dao = new AlunoDAO(mContext);
        Aluno aluno = dao.buscarPorId(id);
        return aluno;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView ivFoto;
        public TextView tNome;
        public TextView tSobrenome;
        public CheckBox cbAtividade;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFoto = (RoundedImageView) itemView.findViewById(R.id.iv_foto);
            tNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tSobrenome = (TextView) itemView.findViewById(R.id.tv_sobrenome);
            cbAtividade = (CheckBox) itemView.findViewById(R.id.cb_aluno_atividade);
        }
    }
}
