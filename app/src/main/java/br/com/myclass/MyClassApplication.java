package br.com.myclass;

import android.app.Application;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by gilmar on 03/09/15.
 */
public class MyClassApplication extends Application {
    private static final String TAG = "MyClassApplication";
    private static MyClassApplication instance = null;
    private Bus bus = new Bus();

    public static MyClassApplication getInstance() {
        return instance; // Singleton
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyClassApplication.onCreate()");
        // Salva a instância para termos acesso como Singleton
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "MyClassApplication.onTerminate()");
    }

    public Bus getBus() {
        return bus;
    }
}
