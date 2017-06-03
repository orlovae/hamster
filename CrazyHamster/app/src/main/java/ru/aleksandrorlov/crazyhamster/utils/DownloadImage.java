package ru.aleksandrorlov.crazyhamster.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 21.05.17.
 */

public class DownloadImage extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String NAME_DIR = "hamsters";
    private final String SCHEME = "file://";
    private final String ERROR_DOWNLOAD = "error_download_image.png";
    private final String FILE_EXTENSION_JPG = "jpg";

    private Context context;
    private TreeMap<Integer, String> mapForDownloadImage;



    public DownloadImage (Context context, TreeMap<Integer, String> mapForDownloadImage){
        this.context = context;
        this.mapForDownloadImage = mapForDownloadImage;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(LOG_TAG, "Start doInBackground");

        File directory;
        ContextWrapper cw = new ContextWrapper(context);
        boolean SDWrite = isExternalStorageWritable();

        if (SDWrite) {
            directory = getAlbumStorageDir(context);
        } else {
            directory = cw.getDir(NAME_DIR, Context.MODE_PRIVATE);
        }

        for (Map.Entry<Integer, String> entry : mapForDownloadImage.entrySet()
             ) {
            String url = entry.getValue();
            Bitmap bitmap = null;
            String fileName = "";
            File path = null;

            if (url != null) {
                try {
                    InputStream inputStream = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    fileName = createNameFile(url);
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception: " + fileName + " no download");
                    bitmap = getErrorDownloadImage();
                    fileName = ERROR_DOWNLOAD;
                    e.printStackTrace();
                }
            } else {
                bitmap = getErrorDownloadImage();
                fileName = ERROR_DOWNLOAD;
            }

            try {
                if (SDWrite) {
                    path = new File(directory, fileName);
                    entry.setValue(path.getAbsolutePath());
                    Log.d(LOG_TAG, "File save to SD, path = " + path.getAbsolutePath().toString().
                            substring(path.getAbsolutePath().toString().length()-3,path.getAbsolutePath().toString().length()));
                } else {
                    path = new File(directory, fileName);
                    entry.setValue(SCHEME + path.getAbsolutePath());
                    Log.d(LOG_TAG, "File save to internal memory, path = " + path.getAbsolutePath().toString());
                }

                FileOutputStream out = new FileOutputStream(path);
                String typeImage = getTypeImage(path);
                Log.d(LOG_TAG, "typeImage.equals(FILE_EXTENSION_JPG) is " + typeImage.equals(FILE_EXTENSION_JPG));
                if (typeImage.equals(FILE_EXTENSION_JPG)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                }

                out.flush();
                out.close();
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception: " + fileName + " no save");
                e.printStackTrace();
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
        ContentValues cv = new ContentValues();
        for (Map.Entry<Integer, String> entry : mapForDownloadImage.entrySet()
                ) {
            int id = entry.getKey();
            cv.put(Contract.Hamster.COLUMN_IMAGE_PATH, entry.getValue());
            Uri uri = ContentUris.withAppendedId(Contract.Hamster.CONTENT_URI, id);

            context.getContentResolver().update(uri, cv, null, null);
            cv.clear();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File getAlbumStorageDir(Context context) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), NAME_DIR);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    private Bitmap getErrorDownloadImage() {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(ERROR_DOWNLOAD);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private String getTypeImage(File path) {
        try {
        /**3 - количество символов в расширении файла**/
        int startSubstring = path.getAbsolutePath().toString().length() - 3;
        int endSubstring = path.getAbsolutePath().toString().length();
        return path.getAbsolutePath().toString().substring(startSubstring, endSubstring);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
