package br.com.myclass.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.com.myclass.R;
import br.com.myclass.fragment.ListAtividadeFragment;

public class AtividadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        if (savedInstanceState == null) {
            ListAtividadeFragment frag = new ListAtividadeFragment();
            Bundle args = getIntent().getExtras();
            frag.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.layout_list_atividade, frag);
            t.commit();
        }
    }

}
