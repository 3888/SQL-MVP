package com.sql.mvp.testapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.application.Application;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.ui.adatpers.UsersListViewAdapter;
import com.sql.mvp.testapp.ui.presenters.UsersPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsersFragment extends BaseFragment implements UsersPresenter.MvpView  {

    @BindView(R.id.users_list)
    ListView list;

    @OnClick(R.id.users_load_db)
    public void loadElementsToDB() {
        presenter.getUsers();
    }

    @OnClick(R.id.users_delete_db)
    public void deleteElementsFromDB() {
        presenter.deleteUsers();
    }

    @Inject
    UsersPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_users, container, false);

        Application
                .getComponent(getContext())
                .inject(this);

        presenter.onAttachView(this, ButterKnife.bind(this, view));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        presenter.onDetachView();
    }

    @Override
    public void initAdapter(List<UsersObject> users) {
        UsersListViewAdapter adapter = new UsersListViewAdapter (getContext(), users);
        list.setAdapter(adapter);
    }


}
