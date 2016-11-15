package com.sql.mvp.testapp.database;

import android.database.Cursor;

import java.util.List;

import rx.Observable;

public interface Database<T> {

    long insertOrUpdateElement(T element);

    List<Long> insertOrUpdateElements(List<T> elements);

    T getElement(String id);

    List<T> getElements();

    List<T> getElements(String where);

    Observable<List<T>> getElementsRx(String where);

    long deleteElement(String id);

    int deleteElements(String where);

    int deleteElements();

    List<T> queryElements(String query);

    Cursor queryCursor(String query);

    int getCount();
}
