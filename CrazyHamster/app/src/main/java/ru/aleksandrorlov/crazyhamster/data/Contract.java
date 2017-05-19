package ru.aleksandrorlov.crazyhamster.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 19.05.17.
 */

public final class Contract {

    public Contract() {
    }

    // Uri authority
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "ru.aleksandrorlov.crazyhamster";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    // path
    public static final class Hamster implements BaseColumns {
        public static final String TABLE_NAME = "Hamster";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URL = "imageURL";
        public static final String COLUMN_FAVORITE = "favorites";

        public static final String PATH_HAMSTER = TABLE_NAME;

        // Общий Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HAMSTER);

        // Типы данных
        // набор строк //
        public static final String TYPE_HAMSTER_ALL_ROW = ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;

        // одна строка
        public static final String TYPE_HAMSTER_SINGLE_ROW = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;
    }
}
