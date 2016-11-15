package com.sql.mvp.testapp.application.components;


import com.sql.mvp.testapp.database.Database;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

public interface DbComponent {

    BriteDatabase briteDatabase();

    BriteContentResolver briteContentResolver();

    SqlBrite sqlBrite();

    Database<UsersObject> pointsDatabase();
}
