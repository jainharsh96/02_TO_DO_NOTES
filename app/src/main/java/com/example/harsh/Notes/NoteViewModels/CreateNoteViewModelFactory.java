package com.example.harsh.Notes.NoteViewModels;

import com.example.harsh.Notes.NoteDatabase.NotesDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class CreateNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private NotesDatabase notesDatabase;
    private final int notesId;

    public CreateNoteViewModelFactory(NotesDatabase database, int id) {
        notesId = id;
        notesDatabase = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NoteViewModel(notesDatabase, notesId);
    }
}
