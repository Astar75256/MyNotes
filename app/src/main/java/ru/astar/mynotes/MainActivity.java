package ru.astar.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Cursor resultCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Uri uri = NoteContentProvider.NOTE_CONTENT_URI;

        resultCursor = getContentResolver().query(uri, null, null, null, null);
        startManagingCursor(resultCursor);

        String from[] = {NoteContentProvider.NOTE_TITLE, NoteContentProvider.NOTE_DESCRIPTION, NoteContentProvider.NOTE_DATE};
        int to[] = {R.id.titleNote, R.id.descriptionNote, R.id.dateNote};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, resultCursor, from, to, 1);
        ListView listNotes = (ListView) findViewById(R.id.listNotes);
        listNotes.setAdapter(cursorAdapter);
        listNotes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, NoteActivity.class);
        int idTable = Integer.parseInt(resultCursor.getString(resultCursor.getColumnIndex(NoteContentProvider.NOTE_ID)));
        if (idTable < 0) return;
        intent.putExtra(NoteActivity.KEY_ID, idTable);
        startActivity(intent);
    }
}
