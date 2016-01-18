package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.Nota;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 30/12/15.
 */
public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.ViewHolder> {
    private List<Nota> mListNotas;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private OnClickListener onClickListener;
    private ImgOnClickListener imgOnClickListener;
    private DeleteOnClickListener deleteOnClickListener;

    public NotaAdapter(List<Nota> listNotas, Activity context, OnClickListener onClickListener, ImgOnClickListener imgOnClickListener, DeleteOnClickListener deleteOnClickListener) {
        this.mListNotas = listNotas;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onClickListener = onClickListener;
        this.imgOnClickListener = imgOnClickListener;
        this.deleteOnClickListener = deleteOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nota, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Aluno aluno = getAluno(position);

        holder.tNome.setText(aluno.getNome() + " " + aluno.getSobrenome());
        holder.tNota.setText("Nota Final: " + calculaNota(mListNotas.get(position).getNota(), mListNotas.get(position).getPontoExtra()));

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

        if(imgOnClickListener != null) {
            holder.ivFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgOnClickListener.onClickImg(v, position);
                }
            });
        }

        if(deleteOnClickListener != null) {
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOnClickListener.onClickDelete(v, position);
                }
            });
        }
    }

    private double calculaNota(String nota, String pontos) {
        if (!nota.equals(".") && !pontos.equals(".") && !nota.equals("") && !nota.equals(null) && !pontos.equals("") && !pontos.equals(null)) {
            return Double.valueOf(nota).doubleValue()+Double.valueOf(pontos);
        } else if (!pontos.equals(".") && !pontos.equals("") && !pontos.equals(null)) {
            return Double.valueOf(pontos).doubleValue();
        } else if (!nota.equals(".") && !nota.equals("") && !nota.equals(null)) {
            return Double.valueOf(nota).doubleValue();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mListNotas != null ? mListNotas.size() : 0;
    }

    private Aluno getAluno(int position) {
        Long id = mListNotas.get(position).getAlunoId();
        AlunoDAO dao = new AlunoDAO(mContext);
        Aluno aluno = dao.buscarPorId(id);
        return aluno;
    }

    public interface OnClickListener {
        public void onClick(View view, int idx);
    }

    public interface ImgOnClickListener {
        public void onClickImg(View view, int idx);
    }

    public interface DeleteOnClickListener {
        public void onClickDelete(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        public TextView tNota;
        public RoundedImageView ivFoto;
        public ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tNota = (TextView) itemView.findViewById(R.id.tv_nota);
            ivFoto = (RoundedImageView) itemView.findViewById(R.id.iv_foto);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

        }
    }
}
