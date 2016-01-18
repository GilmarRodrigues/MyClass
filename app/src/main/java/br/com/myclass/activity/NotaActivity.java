package br.com.myclass.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import br.com.myclass.R;
import br.com.myclass.fragment.ListNotaFragment;

public class NotaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        if (savedInstanceState == null) {
            ListNotaFragment frag = new ListNotaFragment();
            Bundle args = getIntent().getExtras();
            frag.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.layout_list_nota, frag);
            t.commit();
        }
    }

}
