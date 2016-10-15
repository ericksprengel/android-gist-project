package br.com.ericksprengel.aa.retrofit;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by erick on 10/15/16.
 */

public class RetrofitApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
