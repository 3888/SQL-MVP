package com.sql.mvp.testapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

abstract class BaseDatabase<T> implements Database<T> {

    protected static final String COL_ID = "_id";
    protected static final String COL_SERVER_ID = "server_id";
    protected static final String COL_CREATED = "created";
    protected static final String COL_UPDATED = "updated";

    protected final Context mContext;
    protected final BriteDatabase mDatabase;
    private final String mTableName;

    public BaseDatabase(Context context, String tableName, SQLiteOpenHelper database) {
        mContext = context;
        mTableName = tableName;
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabase = sqlBrite.wrapDatabaseHelper(database, Schedulers.io());
    }

    abstract T toObject(Cursor cursor);

    abstract ContentValues toContentValues(T element);

    @Override
    public long insertOrUpdateElement(T element) {
        return mDatabase.insert(mTableName, toContentValues(element));
    }

    @Override
    public List<Long> insertOrUpdateElements(List<T> elements) {
        Timber.e("insertOrUpdateElements");
        List<Long> ids = new ArrayList<>(elements.size());

        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (T element : elements) {
                ids.add(insertOrUpdateElement(element));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return ids;
    }

    @Override
    public T getElement(String id) {
        T element = null;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query("SELECT * FROM " + mTableName + " WHERE " + COL_SERVER_ID + " = ?", id);

            if (cursor.moveToFirst()) {
                element = toObject(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return element;
    }

    @Override
    public List<T> getElements() {
        return getElements(null);
    }

    @Override
    public List<T> getElements(String parameters) {
        return queryElements(getSelectQuery(parameters));
    }

    @Override
    public Observable<List<T>> getElementsRx(String parameters) {
        return mDatabase.createQuery(mTableName, getSelectQuery(parameters))
                .mapToList(this::toObject);
    }

    private String getSelectQuery(String parameters) {
        String query = "SELECT * FROM " + mTableName;
        if (parameters != null) {
            query += " " + parameters;
        }

        return query;
    }

    @Override
    public long deleteElement(String id) {
        return mDatabase.delete(mTableName, COL_SERVER_ID + " = ?", id);
    }

    @Override
    public int deleteElements(String where) {
        return mDatabase.delete(mTableName, where);
    }

    @Override
    public int deleteElements() {
        return mDatabase.delete(mTableName, null);
    }

    @Override
    public List<T> queryElements(String query) {
        List<T> elements = null;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(query);
            elements = new ArrayList<>(cursor.getCount());

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                elements.add(toObject(cursor));
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return elements;
    }

    @Override
    public Cursor queryCursor(String where) {
        return mDatabase.query(where);
    }

    @Override
    public int getCount() {
        int count = 0;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query("SELECT COUNT(*) FROM " + mTableName);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return count;
    }

    protected String toString(long value) {
        return Long.toString(value);
    }

    protected boolean intToBoolean(int value) {
        return value > 0;
    }

    protected int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }
}