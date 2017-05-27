package ru.astar.mynotes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final Uri NOTE_URI = NoteContentProvider.NOTE_CONTENT_URI;

    final String NOTE_TITLE = "title";
    final String NOTE_DESCRIPTION = "description";
    final String NOTE_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cursor cursor = getContentResolver().query(NOTE_URI, null, null, null, null);
        startManagingCursor(cursor);

        String from[] = {NOTE_TITLE, NOTE_DESCRIPTION, NOTE_DATE};
        int to[] = {R.id.titleNote, R.id.descriptionNote, R.id.dateNote};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 1);
        ListView listNotes = (ListView) findViewById(R.id.listNotes);
        listNotes.setAdapter(cursorAdapter);
        listNotes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NoteActivity.KEY_ID, position);

        startActivity(intent);
    }
}
