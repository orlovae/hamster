package ru.aleksandrorlov.crazyhamster.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 19.05.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CrazyHamster.db";

    public static final int TABLE_VERSION = 1;

    private final String SQL_CREATE_HAMSTER_TABLE = "CREATE TABLE "
            + Contract.Hamster.TABLE_NAME + " ("
            + Contract.Hamster.COLUMN_ID + " integer primary key autoincrement,"
            + Contract.Hamster.COLUMN_TITLE + " text,"
            + Contract.Hamster.COLUMN_DESCRIPTION + " text,"
            + Contract.Hamster.COLUMN_IMAGE_URL + " text,"
            + Contract.Hamster.COLUMN_IMAGE_PATH + " text,"
            + Contract.Hamster.COLUMN_FAVORITE + " integer);";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_HAMSTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Hamster.TABLE_NAME);
        onCreate(database);
    }
}
