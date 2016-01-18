package br.com.myclass.fragment.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;

import br.com.myclass.R;
import br.com.myclass.helper.AlunoHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListAluno;
import br.com.myclass.utils.ImageUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 22/09/15.
 */
public class DetalhesAlunoDialog extends BaseDialog {
    private Aluno aluno;
    private RoundedImageView ivFoto;
    private AlunoHelper mHelper;

    public static void show(FragmentManager fm, Aluno aluno) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("detalhes_aluno");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DetalhesAlunoDialog frag = new DetalhesAlunoDialog();
        Bundle args = new Bundle();
        // Passa o objeto por parâmetro
        args.putSerializable(ListAluno.KEY, aluno);
        frag.setArguments(args);
        frag.show(ft, "detalhes_aluno");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Detalhes Aluno");
        View view = inflater.inflate(R.layout.dialog_aluno, container, false);
        mHelper = new AlunoHelper(getActivity(), view);

        ivFoto = (RoundedImageView) view.findViewById(R.id.iv_foto);
        ivFoto.setOnClickListener(onClickFoto());

        view.findViewById(R.id.btn_editar).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cancelar).setVisibility(View.GONE);
        view.findViewById(R.id.layout_buttom).setVisibility(View.GONE);

        aluno = (Aluno) getArguments().getSerializable(ListAluno.KEY);

        if (aluno != null) {
            mHelper.colocaAlunoNoFormulario(aluno);
            mHelper.offCampos();

            if (aluno.getFoto() != null) {
                File file = new File(aluno.getFoto());
                ImageUtils.carregaImagemCamera(file, mHelper, ivFoto.getWidth(), ivFoto.getHeight(), ivFoto);
            } else if (aluno.getGaleria() != null) {
                try {
                    ImageUtils.carregaImagemGaleria(Uri.parse(aluno.getGaleria()), getActivity(), mHelper, ivFoto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return view;
    }

    private View.OnClickListener onClickFoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgAlunoDialog.show(getFragmentManager(), aluno);
            }
        };
    }

}
