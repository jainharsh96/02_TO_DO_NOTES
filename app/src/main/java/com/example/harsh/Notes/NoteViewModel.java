package com.example.harsh.Notes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {

    private static final String TAG = NoteViewModel.class.getSimpleName();

    private LiveData<Note> NotesList;

    public NoteViewModel(NotesDatabase notesDatabase, int id) {

         NotesList = notesDatabase.notesDao().loadNotesById(id);
    }

    public LiveData<Note> getNotesList() {
        return NotesList;
    }
}