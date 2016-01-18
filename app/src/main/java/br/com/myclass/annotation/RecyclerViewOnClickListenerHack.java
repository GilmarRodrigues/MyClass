package br.com.myclass.annotation;

import android.view.View;

/**
 * Created by gilmar on 31/07/15.
 */
public interface RecyclerViewOnClickListenerHack {
    public void onClickListener(View view, int position);
    public void onLongClickListener(View view, int position);
}
