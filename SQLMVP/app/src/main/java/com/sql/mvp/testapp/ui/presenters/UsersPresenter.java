package com.sql.mvp.testapp.ui.presenters;

import android.content.Context;

import com.paginate.Paginate;
import com.sql.mvp.testapp.database.Database;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.ui.fragments.BaseFragment;
import com.sql.mvp.testapp.utils.NameGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class UsersPresenter extends FragmentPresenter<UsersPresenter.MvpView>
        implements Paginate.Callbacks {

    @Inject
    Database<UsersObject> usersDatabase;

    @Inject
    public UsersPresenter(Context context) {
        super(context);
    }

    private boolean loading = false;

    public void loading(boolean loading){
        this.loading = loading;
    }

    public void getUsers(int page) {
        Timber.e("getUsers page = " + page);
        List<UsersObject> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UsersObject usersObject = new UsersObject.Builder()
                    .firstName(NameGenerator.getRandom() + " " + page + i)
                    .lastName(NameGenerator.getRandom() + " " + page + i)
                    .build();
            data.add(i, usersObject);
//            Timber.e("page = " + page);
//            Timber.e("i = " + i);
        }

//        usersDatabase.insertOrUpdateElements(data);
        mvpView.initAdapter(data);
    }

    public void deleteUsers() {
        usersDatabase.deleteElements();
    }

    @Override
    public void onLoadMore() {
        Timber.e("onLoadMore");
        loading = true;
        mvpView.addUserdToDB();

    }

    @Override
    public boolean isLoading() {
        Timber.e("isLoading");
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        Timber.e("hasLoadedAllItems");
        return false;
    }

    public interface MvpView extends BaseFragment.BaseMvpView {
        void initAdapter(List<UsersObject> users);

        void addUserdToDB();
    }
}