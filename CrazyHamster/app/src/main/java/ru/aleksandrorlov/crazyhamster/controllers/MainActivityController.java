package ru.aleksandrorlov.crazyhamster.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.aleksandrorlov.crazyhamster.adapter.CursorAdapter;
import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.model.Hamster;

/**
 * Created by alex on 19.05.17.
 */

public class MainActivityController {
    private static final String LOG_TAG = "MainActivityController";
    private Context context;
    private CursorAdapter cursorAdapter;

    public MainActivityController(Context context) {
        this.context = context;
    }

    public void initDataBase (List<Hamster> hamsters){
        Log.d(LOG_TAG, "Start initDataBase");

        Cursor cursor = context.getContentResolver().query(Contract.Hamster.CONTENT_URI,
                null, null, null, null);

        List<Hamster> hamstersFromCursor = cursorAdapter.getListToCursor(cursor);

        if (!hamsters.equals(hamstersFromCursor)){
            context.getContentResolver().delete(Contract.Hamster.CONTENT_URI, null, null);
            createNewLanguageTable(hamsters);
        }

        initListLanguage(languageDictionare);
        cursor.close();
    }

    private void createNewLanguageTable(List<Hamster> hamsters){
        Log.d(LOG_TAG, "Start createNewLanguageTable");

        for (Language language : listLanguageFromResponse){
            ContentValues cv = new ContentValues();
            cv.put(Contract.Language.COLUMN_CODE_LANGUAGE, language.getCodeLanguage());
            cv.put(Contract.Language.COLUMN_LANGUAGE, language.getLanguage());
            getContentResolver().insert(Contract.Language.CONTENT_URI, cv);
        }
    }

    private void initListLanguage(LanguageDictionare languageDictionare){
        if (listLanguage == null){
            listLanguage = languageDictionare.getListLanguage();
        } else {
            listLanguage.clear();
            listLanguage = languageDictionare.getListLanguage();
        }
    }
}
