package com.sql.mvp.testapp.ui.presenters;

import android.content.Context;

import rx.subscriptions.CompositeSubscription;

abstract class BasePresenter<V> implements Presenter<V> {

    protected final Context context;
    private CompositeSubscription subscriptions;

    V mvpView;

    BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAttachView(V mvpView) {
        this.mvpView = mvpView;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void onDetachView() {
        subscriptions.unsubscribe();
    }

}