package com.sql.mvp.testapp.ui.presenters;

public interface Presenter<V> {
    void onAttachView(V view);

    void onDetachView();
}