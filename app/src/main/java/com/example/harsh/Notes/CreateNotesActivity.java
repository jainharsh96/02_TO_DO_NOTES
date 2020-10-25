package com.example.harsh.Notes;

import com.example.harsh.Notes.NoteDatabase.NotesDatabase;
import com.example.harsh.Notes.NoteModels.Note;
import com.example.harsh.Notes.NoteViewModels.CreateNoteViewModelFactory;
import com.example.harsh.Notes.NoteViewModels.NoteViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class CreateNotesActivity extends BaseActivity {
    public static final String KEY = "instant_key";
    private EditText mBody;
    private Button mSaveData;
    private NotesDatabase notesDatabase;
    private TextView mNoteTitle;
    int DEFAULT_KEY = -1;
    int getKey = DEFAULT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notes_layout);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        notesDatabase = NotesDatabase.getInstance(getApplicationContext());
        mBody = (EditText) findViewById(R.id.Notes_data);
        mSaveData = (Button) findViewById(R.id.Notes_save);
        mNoteTitle = (TextView) findViewById(R.id.NoteTitle);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KEY)) {
            getKey = intent.getIntExtra(KEY, -1);
            CreateNoteViewModelFactory factory = new CreateNoteViewModelFactory(notesDatabase, getKey);
            final NoteViewModel viewModel = ViewModelProviders.of(this, factory).get(NoteViewModel.class);
            viewModel.getNotesList().observe(this, new Observer<Note>() {
                @Override
                public void onChanged(@Nullable Note note) {
                    viewModel.getNotesList().removeObserver(this);
                    populateUI(note);
                }
            });
        }
        mBody.addTextChangedListener(textWatcher);
    }

    public void SaveData(View view) {
        String body = mBody.getText().toString();
        Date date = new Date();
        Note record = new Note(body, date);
        if (getKey == DEFAULT_KEY) {
            notesDatabase.notesDao().inserEntrie(record);
            mSaveData.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            finish();
        } else {
            record.setId(getKey);
            notesDatabase.notesDao().updateEntrie(record);
            finish();
        }
    }

    void populateUI(Note note) {
        if (note == null) {
            return;
        }
        mBody.setText(note.getBody());
        mNoteTitle.setText(getString(R.string.edit_note_title));

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBody.getWindowToken(), 0);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mSaveData.setEnabled(false);
            mSaveData.setBackgroundColor(getResources().getColor(R.color.disable));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mSaveData.setEnabled(true);
            mSaveData.setBackgroundColor(getResources().getColor(R.color.colorUpdate));
        }

        @Override
        public void afterTextChanged(Editable s) {
            String body = mBody.getText().toString().trim();
            if (body.isEmpty()) {
                mSaveData.setEnabled(false);
                mSaveData.setBackgroundColor(getResources().getColor(R.color.disable));
            } else {
                mSaveData.setEnabled(true);
                mSaveData.setBackgroundColor(getResources().getColor(R.color.colorUpdate));
            }
        }
    };
}
