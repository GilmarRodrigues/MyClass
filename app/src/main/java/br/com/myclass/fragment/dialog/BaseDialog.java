package br.com.myclass.fragment.dialog;

import android.support.v4.app.DialogFragment;
import android.view.Window;

import br.com.myclass.R;

/**
 * Created by gilmar on 07/01/16.
 */
public class BaseDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        // Atualiza o tamanho do dialog
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_heigth);
        // getDialog().getWindow().setLayout(width, height);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.white);
    }
}
