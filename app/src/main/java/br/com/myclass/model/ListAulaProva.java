package br.com.myclass.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gilmar on 28/12/15.
 */
public class ListAulaProva implements Serializable{
    public static final String KEY = "aulaprovas";
    public List<AulaProva> mAulaProvas;

    public ListAulaProva(List<AulaProva> mAulaProvas) {
        this.mAulaProvas = mAulaProvas;
    }
}
