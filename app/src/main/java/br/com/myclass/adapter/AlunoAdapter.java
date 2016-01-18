package br.com.myclass.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
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
import br.com.myclass.annotation.RecyclerViewOnClickListenerHack;
import br.com.myclass.dao.AlunoAtividadeDAO;
import br.com.myclass.dao.AtividadeDAO;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.AlunoAtividade;
import br.com.myclass.model.Atividade;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 01/08/15.
 */
public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.ViewHolder> {
    private List<Aluno> mListAlunos;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private PopupMenuOnClickListener popupmenuOnClickListener;
    private ImgOnClickListener imgOnClickListener;

    public AlunoAdapter(List<Aluno> mListAlunos, FragmentActivity context,
                        PopupMenuOnClickListener popupmenuOnClickListener, ImgOnClickListener imgOnClickListener) {
        this.mListAlunos = mListAlunos;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupmenuOnClickListener = popupmenuOnClickListener;
        this.imgOnClickListener = imgOnClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aluno, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap bitmap = null;
        int w = holder.ivFoto.getWidth();
        int h = holder.ivFoto.getHeight();
        if(mListAlunos.get(position).getFoto() != null) {
            File caminhoArquivo = new File(mListAlunos.get(position).getFoto());
            bitmap = ImageUtils.getResizedImage(Uri.fromFile(caminhoArquivo), w, h, false);
        }else if(mListAlunos.get(position).getGaleria() != null){
            try {
                bitmap = ImageUtils.getBitmapFromUri(Uri.parse(mListAlunos.get(position).getGaleria()), mContext, w, h);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bitmap != null){
            holder.ivFoto.setImageBitmap(bitmap);
        }else{
            holder.ivFoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.aluno));
        }

        if (popupmenuOnClickListener != null) {
            holder.ivPopupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupmenuOnClickListener.onClickMenuPopup(v, position);
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

        holder.tvNome.setText(mListAlunos.get(position).getNome() + " " + mListAlunos.get(position).getSobrenome());
        holder.tResumo.setText("Atividades: " +  listAtividadeFeita(position) +"/"+ listAtividade(position));
    }

    @Override
    public int getItemCount() {
        return mListAlunos != null ? mListAlunos.size() : 0;
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack) {
        this.mRecyclerViewOnClickListenerHack = mRecyclerViewOnClickListenerHack;
    }

    public void remove(int position) {
        mListAlunos.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void adicionar(int position, Aluno aluno) {
        mListAlunos.add(aluno);
        notifyItemChanged(position, aluno);
        notifyDataSetChanged();
    }

    private int listAtividade(int position) {
        int total = 0;
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(mContext);
        List<AlunoAtividade> listar = dao.listar(mListAlunos.get(position));
        List<Atividade> atividades = listAtividades();
        for (AlunoAtividade aa: listar) {
            for (Atividade a: atividades) {
                if (aa.getAtividadeId().equals(a.getId())) {
                    total++;
                }
            }
        }
        dao.close();
        return total;
    }

    private int listAtividadeFeita(int position) {
        int total = 0;
        AlunoAtividadeDAO dao = new AlunoAtividadeDAO(mContext);
        List<AlunoAtividade> listar = dao.listarAtividadesFeitas(mListAlunos.get(position));
        List<Atividade> atividades = listAtividades();
        for (AlunoAtividade aa: listar) {
            for (Atividade a: atividades) {
                if (aa.getAtividadeId().equals(a.getId())) {
                    total++;
                }
            }
        }
        dao.close();
        return total;
    }

    private List<Atividade> listAtividades() {
        AtividadeDAO dao = new AtividadeDAO(mContext);
        List<Atividade> list = dao.listAll();
        dao.close();
        return list;
    }


    public interface PopupMenuOnClickListener {
        public void onClickMenuPopup(View view, int idx);
    }

    public interface ImgOnClickListener {
        void onClickImg(View view, int idx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public RoundedImageView ivFoto;
        public TextView tvNome;
        public TextView tResumo;
        public ImageView ivPopupmenu;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFoto = (RoundedImageView) itemView.findViewById(R.id.iv_foto);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tResumo = (TextView) itemView.findViewById(R.id.tv_resumo_ativ_prova);
            ivPopupmenu = (ImageView) itemView.findViewById(R.id.iv_popupmenu);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onLongClickListener(v, getPosition());
            }
            return false;
        }
    }
}
