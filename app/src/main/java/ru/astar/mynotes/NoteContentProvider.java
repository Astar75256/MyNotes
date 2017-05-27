package ru.astar.mynotes;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by molot on 27.05.2017.
 */

public class NoteContentProvider extends ContentProvider {

    // название БД
    public static final String DB_NAME = "notes.db";
    public static final int DB_VERSION = 1;

    // имя таблицы
    public static final String TABLE_NOTES = "notes";

    // поля таблицы
    public static final String NOTE_ID = BaseColumns._ID;
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_DATE = "date";
    public static final String NOTE_DESCRIPTION = "description";

    // запрос на создание таблицы
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NOTES + " (" +
            NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOTE_TITLE + " TEXT, " +
            NOTE_DESCRIPTION + " TEXT, " +
            NOTE_DATE + " TEXT)";

    // Строка авторизации для контент провайдера
    public static final String AUTORITY = "ru.astar.provider.dbnotes";

    // путь
    public static final String NOTE_PATH = "notes";

    // общий (корневой) Uri
    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://"
            + AUTORITY + "/" + NOTE_PATH);

    /* Типы данных */
    // Набор строк
    public static final String NOTE_CONTENT_TYPE = "astar.android.cursor.dir/astar."
            + AUTORITY + "." + NOTE_PATH;

    // одна строка
    public static final String NOTE_CONTENT_ITEM_TYPE = "astar.android.cursor.item/astar."
            + AUTORITY + "." + NOTE_PATH;


    /* Uri Matcher */
    // Общий Uri
    public static final int URI_NOTES = 1;

    // Uri с указанным ID
    public static final int URI_NOTES_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTORITY, NOTE_PATH, URI_NOTES);
        uriMatcher.addURI(AUTORITY, NOTE_PATH + "/#", URI_NOTES_ID);
    }


    DBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectonArgs, @Nullable String sordOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES: // общий Uri
                // ставим сортировку по имени если не указана
                if (TextUtils.isEmpty(sordOrder)) {
                    sordOrder = NOTE_TITLE + " ASC";
                }
                break;

            case URI_NOTES_ID:

                String id = uri.getLastPathSegment();
                Log.d("NoteContentProvider", "URI NOTES ID " + id);

                // добавим ID к условиям выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = NOTE_ID + " =? " + id;
                } else {
                    selection = selection + " AND " + NOTE_ID + " =? " + id;
                }
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, projection, selection, selectonArgs, null, null, sordOrder);
        // уведомляем курсор об изменении данных
        cursor.setNotificationUri(getContext().getContentResolver(), NOTE_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                return NOTE_CONTENT_TYPE;

            case URI_NOTES_ID:
                return NOTE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (uriMatcher.match(uri) != URI_NOTES)
            throw new IllegalArgumentException("Wrong URI " + uri.toString());

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(TABLE_NOTES, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(NOTE_CONTENT_URI, rowID);
        // уведомляем Content Resolver об изменении данных
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:

                break;

            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = NOTE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + NOTE_ID + " = " + id;
                }
                break;

            default:
                throw new IllegalArgumentException("Wrong URI " + uri.toString());
        }

        db = dbHelper.getWritableDatabase();
        int count = db.delete(TABLE_NOTES, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:

                break;

            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = NOTE_ID + " = " + id;
                } else {
                    selection = selection + " AND " + NOTE_ID + " = " + id;
                }
                break;

            default:
                throw new IllegalArgumentException("Wrong URI " + uri.toString());
        }
        db = dbHelper.getWritableDatabase();
        int count = db.update(TABLE_NOTES, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            execSQL(db, CREATE_TABLE_SQL);

            ContentValues values = new ContentValues();
            values.put(NOTE_TITLE, "Note 1");
            values.put(NOTE_DESCRIPTION, "Описание заметки вот какая то заметка там!");
            values.put(NOTE_DATE, "27.05.2017 21:46");
            db.insert(TABLE_NOTES, null, values);

            values.put(NOTE_TITLE, "Note 2");
            values.put(NOTE_DESCRIPTION, "Это уже вторая заметка добавлена немного позже!");
            values.put(NOTE_DATE, "27.05.2017 21:52");
            db.insert(TABLE_NOTES, null, values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            dropTable(db, TABLE_NOTES);
        }


        /**
         * Выполнить запрос
         *
         * @param db
         * @param sql
         * @return
         */
        public boolean execSQL(SQLiteDatabase db, String sql) {
            if (db == null) return false;

            try {
                db.execSQL(sql);
            } catch (SQLiteException e) {
                return false;
            }

            return true;
        }

        /**
         * Удалить таблицу
         *
         * @param db
         * @param table
         * @return
         */
        public boolean dropTable(SQLiteDatabase db, String table) {
            return this.execSQL(db, "DROP TABLE IF EXISTS " + table);
        }

    }
}
