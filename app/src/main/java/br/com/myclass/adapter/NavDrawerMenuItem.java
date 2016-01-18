package br.com.myclass.adapter;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.R;

/**
 * Created by gilmar on 13/08/15.
 */
public class NavDrawerMenuItem {
    // Título: R.string.xxx
    public int title;
    // Figura: R.drawable.xxx
    public int img;
    // Para colocar um fundo cinza quando a linha está selecionada
    public boolean selected;

    public NavDrawerMenuItem(int title, int img) {
        this.title = title;
        this.img = img;
    }

    // Cria a list com itens de menu
    public static List<NavDrawerMenuItem> getList() {
        List<NavDrawerMenuItem> list = new ArrayList<NavDrawerMenuItem>();
        list.add(new NavDrawerMenuItem(R.string.hoje, R.drawable.ic_drawer_brightness));
        list.add(new NavDrawerMenuItem(R.string.turmas, R.drawable.ic_drawer_multiple));
        list.add(new NavDrawerMenuItem(R.string.horario, R.drawable.ic_drawer_clock));
        list.add(new NavDrawerMenuItem(R.string.anotacao, R.drawable.ic_drawer_pencil));
        list.add(new NavDrawerMenuItem(R.string.relatorio, R.drawable.ic_drawer_document));
        list.add(new NavDrawerMenuItem(R.string.lixeira, R.drawable.ic_drawer_delete));
        list.add(new NavDrawerMenuItem(R.string.feedback, R.drawable.ic_drawer_information));
        return list;
    }
}
