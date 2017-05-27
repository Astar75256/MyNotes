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

    Uri URI_NOTE_ID = NoteContentProvider.NOTE_CONTENT_URI;

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
            Uri.parse("content://" + NoteContentProvider.AUTORITY + "/"
                    + NoteContentProvider.NOTE_PATH);

            Log.d("NoteActivity", URI_NOTE_ID.toString());
            Cursor cursor = getContentResolver().query(URI_NOTE_ID, null, BaseColumns._ID , new String[] {String.valueOf(id)}, null);
            Log.d("NoteActivity", "" + cursor.getCount());

cursor.moveToFirst();
                String title = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_DATE));
                titleNote.setText(title);
                descriptionNote.setText(description);
                dateNote.setText(date);


        }
    }
}
