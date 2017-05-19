package ru.aleksandrorlov.crazyhamster.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.helper.DBHelper;

import static ru.aleksandrorlov.crazyhamster.data.Contract.AUTHORITY;
import static ru.aleksandrorlov.crazyhamster.data.Contract.Hamster.PATH_HAMSTER;
import static ru.aleksandrorlov.crazyhamster.data.Contract.Hamster.TYPE_HAMSTER_ALL_ROW;
import static ru.aleksandrorlov.crazyhamster.data.Contract.Hamster.TYPE_HAMSTER_SINGLE_ROW;

/**
 * Created by alex on 19.05.17.
 */

public class HamsterProvider extends ContentProvider {
    private final String LOG_TAG = HamsterProvider.class.getSimpleName();

    /*Table Hamster*/
    private static final int URI_MATCHER_HAMSTER_ALL_ROWS = 1000;
    private static final int URI_MATCHER_HAMSTER_SINGLE_ROW = 1001;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*Table Hamster*/
        uriMatcher.addURI(AUTHORITY, PATH_HAMSTER, URI_MATCHER_HAMSTER_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_HAMSTER + "/#", URI_MATCHER_HAMSTER_SINGLE_ROW);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public void openDatabase () throws SQLiteException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = dbHelper.getReadableDatabase();
        }
    }

    @Override
    public boolean onCreate() {
//        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
//        Log.d(LOG_TAG, "Start query");

        String rowIdHamster;
        String table_name = "";

        openDatabase();

//        Log.d(LOG_TAG, "query uriMatcher.match(uri) = " + uriMatcher.match(uri));

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_HAMSTER_ALL_ROWS:
                table_name = Contract.Hamster.TABLE_NAME;
                break;
            case URI_MATCHER_HAMSTER_SINGLE_ROW:
                table_name = Contract.Hamster.TABLE_NAME;
                rowIdHamster = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Hamster.COLUMN_ID + " = " + rowIdHamster;
                } else {
                    selection = selection + " AND " + Contract.Hamster.COLUMN_ID + " = " +
                            rowIdHamster;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        Cursor cursor = database.query(table_name, projection, selection,
                selectionArgs, null, null, sortOrder);

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
//        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {

            /*Table Hamster*/
            case URI_MATCHER_HAMSTER_ALL_ROWS:
                return TYPE_HAMSTER_ALL_ROW;
            case URI_MATCHER_HAMSTER_SINGLE_ROW:
                return TYPE_HAMSTER_SINGLE_ROW;
           default:
                throwIllegalArgumentException(uri);
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        Log.d(LOG_TAG, "Start insert");
//        Log.d(LOG_TAG, " insert uriMatcher.match(uri) = " + uriMatcher.match(uri));

        long rowIDHamster;

        String table_name = "";

        Uri resultUri = null;

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Hamster*/
            case URI_MATCHER_HAMSTER_ALL_ROWS:
                table_name = Contract.Hamster.TABLE_NAME;
                rowIDHamster = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDHamster);
                break;
            case URI_MATCHER_HAMSTER_SINGLE_ROW:
                table_name = Contract.Hamster.TABLE_NAME;
                rowIDHamster = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDHamster);
        }

        try {
            getContext().getContentResolver().notifyChange(resultUri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        }
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        Log.d(LOG_TAG, "Start delete");
//        Log.d(LOG_TAG, "delete uriMatcher.match(uri) = " + uriMatcher.match(uri));

        String rowIDHamster;

        int countRowsDelete = -1;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Language*/
            case URI_MATCHER_HAMSTER_ALL_ROWS:
                table_name = Contract.Hamster.TABLE_NAME;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_HAMSTER_SINGLE_ROW:
                table_name = Contract.Hamster.TABLE_NAME;
                rowIDHamster = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Hamster.COLUMN_ID + " = " + rowIDHamster;
                } else {
                    selection = selection + " AND " + Contract.Hamster.COLUMN_ID + " = "
                            + rowIDHamster;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        }
        return countRowsDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
//        Log.d(LOG_TAG, "Start delete");
//        Log.d(LOG_TAG, "query, " + uri.toString());
        String rowIDHamster;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Language*/
            case URI_MATCHER_HAMSTER_ALL_ROWS:
                table_name = Contract.Hamster.TABLE_NAME;
                break;
            case URI_MATCHER_HAMSTER_SINGLE_ROW:
                table_name = Contract.Hamster.TABLE_NAME;
                rowIDHamster = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Hamster.COLUMN_ID + " = " + rowIDHamster;
                } else {
                    selection = selection + " AND " + Contract.Hamster.COLUMN_ID + " = "
                            + rowIDHamster;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        int countRowsUpdate = database.update(table_name, values, selection, selectionArgs);
        Log.d(LOG_TAG, "countRowsUpdate = " + countRowsUpdate);
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        }
        return countRowsUpdate;
    }

    private void throwIllegalArgumentException (Uri uri){
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
}
