package br.com.myclass.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.com.myclass.R;


/**
 * Created by gilmar on 30/07/15.
 */
public class BaseActivity extends livroandroid.lib.activity.BaseActivity {

    // Configura a Toolbar
    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void snackbar(String text, String button, View view) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction(button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                .show();

        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }

}
