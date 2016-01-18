package br.com.myclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import br.com.myclass.R;
import br.com.myclass.adapter.TabsAdapter;
import br.com.myclass.fragment.ListAlunoFragment;
import br.com.myclass.fragment.ListAulaFragment;
import br.com.myclass.fragment.ListProvaFragment;
import br.com.myclass.fragment.dialog.AdicionarAlunoDialog;
import br.com.myclass.fragment.dialog.AdicionarAulaDialog;
import br.com.myclass.fragment.dialog.AdicionarProvaDialog;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.Aula;
import br.com.myclass.model.ListTurma;
import br.com.myclass.model.Prova;
import br.com.myclass.model.Turma;

public class TabActivity extends BaseActivity implements View.OnClickListener{
    private int position;
    private FrameLayout fab;
    private ImageButton fabBtn;
    private Turma mTurma;
    private ViewPager viewPager;
    private ListAlunoFragment fragListAlunos;
    private ListAulaFragment fragListAulas;
    private ListProvaFragment fragListProvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setUpToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getActivity().getIntent();
        mTurma = (Turma) intent.getSerializableExtra(ListTurma.KEY);
        getSupportActionBar().setTitle(mTurma.getDisciplina());
        getSupportActionBar().setSubtitle(mTurma.getCurso());

        fab = (FrameLayout) findViewById(R.id.myfab_main);
        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        fabBtn.setOnClickListener(this);

        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
        fab.setAnimation(animation);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
                fab.clearAnimation();
                fab.setAnimation(animation);
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        position = 0;
                        break;
                    case 1:
                        position = 1;
                        break;
                    case 2:
                        position = 2;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //fab.setAnimation(animation);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        fragListAlunos = new ListAlunoFragment();
        fragListAulas = new ListAulaFragment();
        fragListProvas = new ListProvaFragment();
        adapter.addFrag(fragListAlunos, "ALUNOS");
        adapter.addFrag(fragListAulas, "AULAS");
        adapter.addFrag(fragListProvas, "AVALIAÇÕES");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(final View v) {
        if(position == 0) {
            AdicionarAlunoDialog.show(getSupportFragmentManager(), mTurma, new AdicionarAlunoDialog.Callback() {
                @Override
                public void onAlunoAdd(Aluno aluno) {
                    fragListAlunos.carregarLista();
                    snackbar("Aluno salvo com sucesso.", "FECHAR", fragListAlunos.mRecyclerView);
                }
            });
        } else if (position == 1) {
            AdicionarAulaDialog.show(getSupportFragmentManager(), mTurma, new AdicionarAulaDialog.Callback() {
                @Override
                public void onAulaAdd(Aula aula) {
                    fragListAulas.carregaLista();
                    snackbar("Aula salva com sucesso.", "FECHAR", fragListAlunos.mRecyclerView);
                }
            });
        } else {
            AdicionarProvaDialog.show(getSupportFragmentManager(), mTurma, new AdicionarProvaDialog.Callback() {
                @Override
                public void onProvaAdd(Prova prova) {
                    fragListProvas.carregaLista();
                    snackbar("Avaliação salva com sucesso.", "FECHAR", fragListAlunos.mRecyclerView);
                }
            });
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
