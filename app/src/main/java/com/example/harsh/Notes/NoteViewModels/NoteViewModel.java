package com.example.harsh.Notes.NoteViewModels;

import com.example.harsh.Notes.NoteDatabase.NotesDatabase;
import com.example.harsh.Notes.NoteModels.Note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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