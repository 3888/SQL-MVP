package com.sql.mvp.testapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.sql.mvp.testapp.server.models.UsersObject;
import com.sql.mvp.testapp.utils.data.UsersData;

public class UsersDatabase extends BaseDatabase<UsersData> {

    public static final String TABLE_NAME = "data";

    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";


    public UsersDatabase(Context context, SQLiteOpenHelper database) {
        super(context, TABLE_NAME, database);
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY, " +
                    COL_FIRST_NAME + " TEXT, " +
                    COL_LAST_NAME + " TEXT " +
                    ");";

    @Override
    UsersData toObject(Cursor cursor) {
        UsersData.Builder builder = new UsersData.Builder();
        builder.firstName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME)));
        builder.lastName(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME)));

        return builder.build();
    }

    @Override
    ContentValues toContentValues(UsersData element) {
        ContentValues values = new ContentValues();
        values.put(COL_FIRST_NAME, element.getFirstName());
        values.put(COL_LAST_NAME, element.getLastName());

        return values;
    }
}
