package com.sql.mvp.testapp.ui.presenters;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    protected void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    protected void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    protected String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    protected void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
    }
}