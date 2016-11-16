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

import butterknife.Unbinder;

public class UsersPresenter extends FragmentPresenter<UsersPresenter.MvpView>
        implements Paginate.Callbacks {

    @Inject
    UsersPresenter(Context context, Database<UsersObject> usersDatabase) {
        super(context);
        this.usersDatabase = usersDatabase;
    }

    private Database<UsersObject> usersDatabase;
    private boolean loading;

    @Override
    public void onAttachView(MvpView mvpView, Unbinder unbinder) {
        super.onAttachView(mvpView, unbinder);
    }

    @Override
    public void onLoadMore() {
        loading = true;
        mvpView.addUsersToDB();
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return false;
    }

    public interface MvpView extends BaseFragment.BaseMvpView {
        void initAdapter(List<UsersObject> users);

        void addUsersToDB();
    }

    public void getUsers(int page, boolean needPagination) {
        if (needPagination) {
            List<UsersObject> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                UsersObject usersObject = new UsersObject.Builder()
                        .firstName(NameGenerator.getRandom() + " " + page + i)
                        .lastName(NameGenerator.getRandom() + " " + page + i)
                        .build();
                data.add(i, usersObject);
            }

            usersDatabase.insertOrUpdateElements(data);
            mvpView.initAdapter(data);
        } else {
            mvpView.initAdapter(usersDatabase.getElements());
        }
    }

    public void deleteUsers() {
        usersDatabase.deleteElements();
    }

    public void loading(boolean loading) {
        this.loading = loading;
    }
}