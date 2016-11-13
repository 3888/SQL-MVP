package com.sql.mvp.testapp.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.application.Application;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.ui.adatpers.UsersAdapter;
import com.sql.mvp.testapp.ui.presenters.UsersPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsersFragment extends BaseFragment implements UsersPresenter.MvpView {

    @BindView(R.id.users_list)
    RecyclerView recyclerView;

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

//        Application
//                .getComponent(getContext())
//                .inject(this);

        presenter.onAttachView(this, ButterKnife.bind(this, view));

//        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetachView();
    }

    @Override
    public void initAdapter(List<UsersObject> users) {
        UsersAdapter adapter = new UsersAdapter(getContext(), users);
        recyclerView.setAdapter(adapter);
    }
}