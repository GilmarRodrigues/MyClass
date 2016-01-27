package br.com.myclass.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.List;

import br.com.myclass.R;
import br.com.myclass.adapter.NavDrawerMenuAdapter;
import br.com.myclass.adapter.NavDrawerMenuItem;
import br.com.myclass.fragment.ListAnotacaoFragment;
import br.com.myclass.fragment.ListHojeFragment;
import br.com.myclass.fragment.ListHorarioFragment;
import br.com.myclass.fragment.ListLixeiraFragment;
import br.com.myclass.fragment.ListRelatoriosFragment;
import br.com.myclass.fragment.ListTurmaFragment;
import br.com.myclass.fragment.dialog.AboutDialog;
import livroandroid.lib.fragment.NavigationDrawerFragment;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavDrawerFragment;
    private NavDrawerMenuAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        //Icon
        getSupportActionBar().setIcon(R.drawable.ic_action_myclass);
        getSupportActionBar().setTitle("");
        // Nav Drawer
        mNavDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        // Configura o drawer layout
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawerFragment.setUp(drawerLayout);

    }

    @Override
    public NavigationDrawerFragment.NavDrawerListView
    getNavDrawerView(NavigationDrawerFragment navigationDrawerFragment,
                     LayoutInflater layoutInflater, ViewGroup container) {
        // Deve retornar a view e o identificador do listView
        View view = layoutInflater.inflate(R.layout.nav_drawer_listview, container, false);
        // Preecha cabeçalho com a foto, nome e email.
        navigationDrawerFragment.setHeaderValues(view, R.id.listViewContainer,
                R.drawable.nav_drawer_header, R.mipmap.ic_launcher, R.string.nav_drawer_username,
                R.string.nav_drawer_email);
        return new NavigationDrawerFragment.NavDrawerListView(view, R.id.listView);
    }

    @Override
    public ListAdapter getNavDrawerListAdapter(NavigationDrawerFragment navigationDrawerFragment) {
        // Este método deve retornar o adapter que vai preencher o listView
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        // Deixa o primeiro item selecionado
        list.get(0).selected = true;
        this.listAdapter = new NavDrawerMenuAdapter(this, list);
        return listAdapter;
    }

    @Override
    public void onNavDrawerItemSelected(NavigationDrawerFragment navigationDrawerFragment,
                                        int position) {
        // Método chamado ao selecionar um item do listView
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        NavDrawerMenuItem selectedItem = list.get(position);
        // Seleciona a linha
        this.listAdapter.setSelected(position, true);
        if (position == 0) {
            replaceFragment(new ListHojeFragment());
        } else if (position == 1) {
            replaceFragment(new ListTurmaFragment());
        } else if (position == 2) {
            replaceFragment(new ListHorarioFragment());
        } else if (position == 3) {
            replaceFragment(new ListAnotacaoFragment());
        } else if (position == 4) {
            replaceFragment(new ListRelatoriosFragment());
        } else if (position == 5) {
            replaceFragment(new ListLixeiraFragment());
        } else if (position == 6) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "contato@myclassapp.com.br", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forneça-nos um Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Escreva suas sugestões");
            startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
        } else {
            Log.e("MyClass", "Item de menu inválido");
        }
    }

    // Adiciona fragment no centro da tela
    private void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_drawer_cotainer,
                frag, "TAG").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AboutDialog.showAbout(getSupportFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}