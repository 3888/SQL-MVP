package com.sql.mvp.testapp.ui.presenters;

import android.content.Context;

import butterknife.Unbinder;

public abstract class FragmentPresenter<V> extends BasePresenter<V> {

    private Unbinder unbinder;

    FragmentPresenter(Context context) {
        super(context);
    }

    public void onAttachView(V mvpView, Unbinder unbinder) {
        super.onAttachView(mvpView);

        this.unbinder = unbinder;
    }

    @Override
    public void onDetachView() {
        super.onDetachView();

        unbinder.unbind();
    }
}