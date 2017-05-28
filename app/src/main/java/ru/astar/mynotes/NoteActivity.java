package ru.astar.mynotes;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.URI;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_ID = "id";

    private TextView titleNote;
    private TextView descriptionNote;
    private TextView dateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initUI();
    }

    private void initUI() {
        titleNote = (TextView) findViewById(R.id.titleNote);
        descriptionNote = (TextView) findViewById(R.id.descriptionNote);
        dateNote = (TextView) findViewById(R.id.dateNote);

        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra(KEY_ID, 0);
            String uriPath = "content://"
                    + NoteContentProvider.AUTORITY + "/"
                    + NoteContentProvider.NOTE_PATH + "/#" + id;

            Log.d("NoteActivity", uriPath);

            Uri uri = Uri.parse(uriPath);
            Cursor cursor = getContentResolver().query(uri, null, "_id = ?", new String[] {String.valueOf(id)}, null);
            Log.d("NoteActivity", "count: " + cursor.getCount());
            cursor.moveToFirst();
            String string = "";
            Log.d("NoteActivity", string = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_TITLE)));
            titleNote.setText(string);
        }
    }
}
