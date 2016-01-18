package br.com.myclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

import br.com.myclass.R;
import br.com.myclass.fragment.SampleSlideFragment;

/**
 * Created by gilmar on 07/01/16.
 */
public class DefaultIntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlideFragment.newInstance(R.layout.intro));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4));

        setSkipText("Pular");
        setDoneText("Concluído");
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
        //Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "onSkipPressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "onDonePressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlideChanged() {
    }

    public void getStarted(View v){
        loadMainActivity();
    }
}