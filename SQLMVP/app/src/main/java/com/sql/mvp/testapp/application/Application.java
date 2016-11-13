package com.sql.mvp.testapp.application;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sql.mvp.testapp.BuildConfig;
import com.sql.mvp.testapp.application.components.AppComponent;
import com.sql.mvp.testapp.application.components.DaggerAppComponent;
import com.sql.mvp.testapp.application.modules.AppModule;
import com.sql.mvp.testapp.application.modules.DbModule;
import com.sql.mvp.testapp.database.DatabaseHelper;


import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class Application extends android.app.Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule(new DatabaseHelper(this)))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
        }
    }

    public static AppComponent getComponent(Context context) {
        return ((Application) context.getApplicationContext()).appComponent;
    }
}