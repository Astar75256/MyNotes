package ru.astar.mynotes.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.astar.mynotes.NoteContentProvider;
import ru.astar.mynotes.R;

/**
 * Created by molot on 28.05.2017.
 */

public class AddNoteFragment extends Fragment implements View.OnClickListener {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_note_fragment_view, container);

        titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(addButton)) {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.toString();

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(description)) return;

            ContentValues values = new ContentValues();
            values.put(NoteContentProvider.NOTE_TITLE, title);
            values.put(NoteContentProvider.NOTE_DESCRIPTION, description);
            values.put(NoteContentProvider.NOTE_DATE, new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date().getTime()));

            getContext().getContentResolver().insert(NoteContentProvider.NOTE_CONTENT_URI, values);
        }
    }
}
