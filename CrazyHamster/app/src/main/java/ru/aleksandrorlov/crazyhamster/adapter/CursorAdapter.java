package ru.aleksandrorlov.crazyhamster.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.model.Hamster;

/**
 * Created by alex on 19.05.17.
 */

public class CursorAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Hamster> hamsters;
    private Hamster hamster;

    public List<Hamster> getListToCursor(Cursor cursor, boolean onLikeHamster){
        Log.d(LOG_TAG, "Start getListToCursor");
        if (hamsters == null) {
            hamsters = new ArrayList<>();
        } else {
            hamsters.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int titleColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_TITLE);
                int descriptionColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_DESCRIPTION);
                int imageURLColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_IMAGE_URL);
                int favoriteColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_FAVORITE);
                do {
                    String titleFromCursor = cursor.getString(titleColIndex);
                    String descripionFromCursor = cursor.getString(descriptionColIndex);
                    String imageURLFromCursor = cursor.getString(imageURLColIndex);
                    if (onLikeHamster) {
                        int favorite = cursor.getInt(favoriteColIndex);
                        boolean favoriteFromCursor = castIntToBoolean(favorite);

//                        hamster = new Hamster(titleFromCursor, descripionFromCursor,
//                            imageURLFromCursor, favoriteFromCursor);
                    } else {
                        hamster = new Hamster(titleFromCursor, descripionFromCursor,
                                imageURLFromCursor);
                    }


                    hamsters.add(hamster);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return hamsters;
    }

    private boolean castIntToBoolean(int favoriteFromCursor){
        if (favoriteFromCursor == 0){
            return false;
        } else {
            return true;
        }
    }
}
