package com.sql.mvp.testapp.ui.presenters;

import android.content.Context;

import com.sql.mvp.testapp.database.Database;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.ui.fragments.BaseFragment;
import com.sql.mvp.testapp.utils.NameGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UsersPresenter extends FragmentPresenter<UsersPresenter.MvpView> {

    private static final int ELEMENTS_SIZE = 1000;

    @Inject
    Database<UsersObject> usersDatabase;

    @Inject
    public UsersPresenter(Context context) {
        super(context);

    }

    public void getUsers() {
            List<UsersObject> data = new ArrayList<>();
            for (int i = 0; i < ELEMENTS_SIZE; i++) {

                UsersObject usersObject = new UsersObject.Builder()
                        .firstName(NameGenerator.getRandomName())
                        .lastName(String.valueOf(i))
                        .build();
                data.add(i, usersObject);
        }

        usersDatabase.insertOrUpdateElements(data);
        mvpView.initAdapter(usersDatabase.getElements());
    }

    public void deleteUsers(){
        usersDatabase.deleteElements();
        mvpView.initAdapter(usersDatabase.getElements());
    }

    public interface MvpView extends BaseFragment.BaseMvpView {
        void initAdapter(List<UsersObject> users);
    }
}