package ru.aleksandrorlov.crazyhamster.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alex on 21.05.17.
 */

public class DownloadImage extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context context;
    private TreeMap<Integer, String> mapForDownloadImage;

    public DownloadImage (Context context, TreeMap<Integer, String> mapForDownloadImage){
        this.context = context;
        this.mapForDownloadImage = mapForDownloadImage;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(LOG_TAG, "Start doInBackground");

        for (Map.Entry<Integer, String> entry : mapForDownloadImage.entrySet()
             ) {
            String url = entry.getValue();
            Bitmap bitmap = null;
            if (url != null) {
                try {
                    InputStream inputStream = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception: " + createNameFile(url) + " no download");
                    e.printStackTrace();
                }
                try {
                    ContextWrapper cw = new ContextWrapper(context);
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File path = new File(directory, createNameFile(url));
                    Log.d(LOG_TAG, "path: " + path.getAbsolutePath());
                    entry.setValue(path.getAbsolutePath());
                    FileOutputStream out = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception: " + createNameFile(url) + " no save");
                    e.printStackTrace();
                }
        } else {
// url == null!!!
            }
        }
        return null;
    }

    private String createNameFile(String imageURL){
        Uri uri = Uri.parse(imageURL);
        return uri.getLastPathSegment();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //здесь нужно писать метод добавления в базу данных путей для картинок.
        //может для картинок, которые выбросили исключение, сделать свою картинку в ресурсах, путь к
        //которой писать в базу.

    }
}
