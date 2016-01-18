package br.com.myclass.fragment.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import br.com.myclass.R;
import br.com.myclass.helper.AlunoHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListAluno;
import br.com.myclass.utils.ImageUtils;

/**
 * Created by gilmar on 20/12/15.
 */
public class ImgAlunoDialog extends DialogFragment {
    private Aluno aluno;
    private ImageView ivFoto;
    private AlunoHelper mHelper;

    public static void show(FragmentManager fm, Aluno aluno) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("img_aluno");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ImgAlunoDialog frag = new ImgAlunoDialog();
        Bundle args = new Bundle();
        // Passa o objeto por parâmetro
        args.putSerializable(ListAluno.KEY, aluno);
        frag.setArguments(args);
        frag.show(ft, "img_aluno");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        // Atualiza o tamanho do dialog
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width_img);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_heigth_img);
        getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationImg;
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.white);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_aluno_img, container, false);
        mHelper = new AlunoHelper(getActivity(), view);
        ivFoto = (ImageView) view.findViewById(R.id.iv_foto);
        ivFoto.setAdjustViewBounds(true);
        ivFoto.setScaleType(ImageView.ScaleType.FIT_XY);


        aluno = (Aluno) getArguments().getSerializable(ListAluno.KEY);

        if (aluno != null) {

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

}
