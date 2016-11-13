package com.sql.mvp.testapp.application.modules;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;


import com.sql.mvp.testapp.database.Database;
import com.sql.mvp.testapp.database.UsersDatabase;
import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.utils.data.UsersData;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module
public final class DbModule {

    private final SQLiteOpenHelper sqliteOpenHelper;

    public DbModule(SQLiteOpenHelper sqliteOpenHelper) {
        this.sqliteOpenHelper = sqliteOpenHelper;
    }

    @Provides
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper() {
        return sqliteOpenHelper;
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create();
    }

    @Provides
    @Singleton
    BriteDatabase provideBriteDatabase(SqlBrite sqlBrite, SQLiteOpenHelper openHelper) {
        return sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }

    @Provides
    @Singleton
    BriteContentResolver provideBriteContentResolver(SqlBrite sqlBrite, Context context) {
        return sqlBrite.wrapContentProvider(context.getContentResolver(), Schedulers.io());
    }

    @Provides
    @Singleton
    Database<UsersData> providePointsDatabase(Context context, SQLiteOpenHelper sqliteOpenHelper) {
        return new UsersDatabase(context, sqliteOpenHelper);
    }
}
