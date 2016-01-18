package br.com.myclass.fragment;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.myclass.R;
import br.com.myclass.utils.MyRecyclerScroll;

/**
 * Created by gilmar on 30/07/15.
 */
public class BaseFragment extends livroandroid.lib.fragment.BaseFragment {

    protected void setUpToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
    }

    public void snackbar(String text, String button, RecyclerView recyclerView) {
        Snackbar.make(recyclerView, text, Snackbar.LENGTH_LONG)
                .setAction(button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getActivity().getResources().getColor(R.color.accent))
                .show();
    }

    public void fab(RecyclerView recyclerView, View view, ImageButton fabBtn){
        final int fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
        final FrameLayout fab = (FrameLayout) view.findViewById(R.id.myfab_main);
        fabBtn = getFabButton(fabBtn, view);
        View fabShadow = view.findViewById(R.id.myfab_shadow);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);

        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getActivity().getDrawable(R.drawable.ripple_accent));
        }

        fab.startAnimation(animation);
    };

    public ImageButton getFabButton(ImageButton fabBtn, View view) {
        fabBtn = (ImageButton) view.findViewById(R.id.myfab_main_btn);
        return fabBtn;
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
