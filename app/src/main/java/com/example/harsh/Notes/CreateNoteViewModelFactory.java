package com.example.harsh.Notes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class CreateNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private NotesDatabase notesDatabase;
    private final int notesId;

    public CreateNoteViewModelFactory(NotesDatabase database, int id){
        notesId = id;
        notesDatabase = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NoteViewModel(notesDatabase, notesId);
    }
}
