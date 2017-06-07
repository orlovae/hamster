package ru.aleksandrorlov.crazyhamster;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.crazyhamster.adapter.CursorAdapter;
import ru.aleksandrorlov.crazyhamster.controllers.ApiController;
import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.model.Hamster;
import ru.aleksandrorlov.crazyhamster.rest.ApiUnrealMojo;
import ru.aleksandrorlov.crazyhamster.utils.DownloadImage;

/**
 * Created by alex on 21.05.17.
 */

public class App extends Application {
    private ApiUnrealMojo apiUnrealMojo;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        apiUnrealMojo = ApiController.getApi();
        apiUnrealMojo.getHamsters().enqueue(new Callback<List<Hamster>>(){

            @Override
            public void onResponse(Call<List<Hamster>> call, Response<List<Hamster>> response) {
                try {
                    if (response.isSuccessful()){
                        List<Hamster> hamsters = response.body();
                        checkDataBase(hamsters);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Hamster>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void checkDataBase (List<Hamster> hamsters){
        String[] projection = {Contract.Hamster.COLUMN_TITLE, Contract.Hamster.COLUMN_DESCRIPTION,
                Contract.Hamster.COLUMN_IMAGE_URL};

        Cursor cursor = getContentResolver().query(Contract.Hamster.CONTENT_URI,
                projection, null, null, null);

        CursorAdapter cursorAdapter = new CursorAdapter();
        List<Hamster> hamstersFromCursor = cursorAdapter.getListToCursor(cursor, false);

        if (!hamsters.equals(hamstersFromCursor)){
            getContentResolver().delete(Contract.Hamster.CONTENT_URI, null, null);
            createNewHamsterTable(hamsters);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void createNewHamsterTable(List<Hamster> hamsters){
        for (Hamster item:hamsters
                ) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.Hamster.COLUMN_TITLE, item.getTitle());
            cv.put(Contract.Hamster.COLUMN_DESCRIPTION, item.getDescription());
            cv.put(Contract.Hamster.COLUMN_IMAGE_URL, item.getImageURL());
            getContentResolver().insert(Contract.Hamster.CONTENT_URI, cv);
        }
        downloadImage();
    }

    private void downloadImage () {
        TreeMap<Integer, String> mapForDownloadImage = new TreeMap<>();

        String[] projection = {Contract.Hamster.COLUMN_ID, Contract.Hamster.COLUMN_IMAGE_URL};

        Cursor cursor = getContentResolver().query(Contract.Hamster.CONTENT_URI,
                projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()){
                int idColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_ID);
                int imageURLColIndex = cursor.getColumnIndex(Contract.Hamster.COLUMN_IMAGE_URL);
                do {
                    Integer idFromCursor = cursor.getInt(idColIndex);
                    String imageURLFromCursor = cursor.getString(imageURLColIndex);
                    mapForDownloadImage.put(idFromCursor, imageURLFromCursor);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
            DownloadImage downloadImage = new DownloadImage(getApplicationContext(),
                    mapForDownloadImage);
            downloadImage.execute();
    }
}
