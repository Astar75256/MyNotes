package ru.astar.mynotes;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ru.astar.mynotes.fragments.AddNoteFragment;
import ru.astar.mynotes.fragments.EditNoteFragment;
import ru.astar.mynotes.fragments.NoteFragment;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_ID = "id";

    public static final int CODE_ADD_NOTE = 100;
    public static final int CODE_ITEM_NOTE = 101;
    public static final int CODE_EDIT_NOTE = 102;

    private TextView titleNote;
    private TextView descriptionNote;
    private TextView dateNote;


    private AddNoteFragment addNoteFragment;
    private EditNoteFragment editNoteFragment;
    private NoteFragment noteFragment;

    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initUI();

        //initOldUI();
    }

    private void initUI() {
        addNoteFragment = new AddNoteFragment();
        editNoteFragment = new EditNoteFragment();
        noteFragment = new NoteFragment();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerFragment, noteFragment);
        transaction.show(noteFragment);
        transaction.commit();
    }

    private void initOldUI() {
        titleNote = (TextView) findViewById(R.id.titleNote);
        descriptionNote = (TextView) findViewById(R.id.descriptionNote);
        dateNote = (TextView) findViewById(R.id.dateNote);

        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra(KEY_ID, 0);
            String uriPath = "content://"
                    + NoteContentProvider.AUTORITY + "/"
                    + NoteContentProvider.NOTE_PATH + "#" + id;

            Uri uri = Uri.parse(uriPath);

            Cursor cursor = getContentResolver().query(
                    uri,
                    null,
                    BaseColumns._ID + "=?",
                    new String[] {String.valueOf(id)},
                    null);



            if (cursor.moveToFirst()) {

                String title = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(NoteContentProvider.NOTE_DATE));

                titleNote.setText(title);
                descriptionNote.setText(description);
                dateNote.setText(date);
            }
        }
    }
}
