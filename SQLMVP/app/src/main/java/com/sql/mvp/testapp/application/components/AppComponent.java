package com.sql.mvp.testapp.application.components;

import com.sql.mvp.testapp.application.modules.AppModule;
import com.sql.mvp.testapp.application.modules.DbModule;
import com.sql.mvp.testapp.ui.fragments.UsersFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        DbModule.class
})
public interface AppComponent {

    void inject(UsersFragment fragment);
}