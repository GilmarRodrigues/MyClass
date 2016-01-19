package br.com.myclass.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import br.com.myclass.R;
import livroandroid.lib.utils.AndroidUtils;

/**
 * Created by gilmar on 19/01/16.
 */
public class InformationNotaAtividadesDialog extends DialogFragment {

    // Método utilitário para mostrar o dialog
    public static void showInformation(android.support.v4.app.FragmentManager fm){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_information");
        if(prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new InformationNotaAtividadesDialog().show(ft, "dialog_information");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Cria HTML com o texto de about
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(Html.fromHtml(getString(R.string.information_dialog_text)));
        // Infla o layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_information, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());
        // Cria o dialog customizado
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.information_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create();
    }
}
