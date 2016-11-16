package com.sql.mvp.testapp.ui.presenters;

interface Presenter<V> {
    void onAttachView(V view);

    void onDetachView();
}