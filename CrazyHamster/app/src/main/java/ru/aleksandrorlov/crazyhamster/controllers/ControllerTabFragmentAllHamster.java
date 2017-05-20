package ru.aleksandrorlov.crazyhamster.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.List;

import ru.aleksandrorlov.crazyhamster.adapter.CursorAdapter;
import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.model.Hamster;

/**
 * Created by alex on 19.05.17.
 */

public class ControllerTabFragmentAllHamster {
    private static final String LOG_TAG = "MainActivityController";
    private Context context;

    public ControllerTabFragmentAllHamster(Context context) {
        this.context = context;
    }

    public void checkDataBase (List<Hamster> hamsters){
        Log.d(LOG_TAG, "Start initDataBase");

        Cursor cursor = context.getContentResolver().query(Contract.Hamster.CONTENT_URI,
                null, null, null, null);

        CursorAdapter cursorAdapter = new CursorAdapter();
        List<Hamster> hamstersFromCursor = cursorAdapter.getListToCursor(cursor, false);

        if (!hamsters.equals(hamstersFromCursor)){
            context.getContentResolver().delete(Contract.Hamster.CONTENT_URI, null, null);
            createNewHamsterTable(hamsters);
        }
        cursor.close();
    }

    private void createNewHamsterTable(List<Hamster> hamsters){
        Log.d(LOG_TAG, "Start createNewLanguageTable");

        for (Hamster item:hamsters
                ) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.Hamster.COLUMN_TITLE, item.getTitle());
            cv.put(Contract.Hamster.COLUMN_DESCRIPTION, item.getDescription());
            cv.put(Contract.Hamster.COLUMN_IMAGE_URL, item.getImageURL());
            context.getContentResolver().insert(Contract.Hamster.CONTENT_URI, cv);
        }
    }
}
