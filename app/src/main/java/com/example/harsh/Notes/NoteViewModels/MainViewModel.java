package com.example.harsh.Notes.NoteViewModels;

import com.example.harsh.Notes.NoteDatabase.NotesDatabase;
import com.example.harsh.Notes.NoteModels.Note;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Note>> tasks;

    public MainViewModel(Application application) {
        super(application);
        NotesDatabase database = NotesDatabase.getInstance(this.getApplication());
        tasks = database.notesDao().loadAllNotes();
    }

    public LiveData<List<Note>> getTasks() {
        return tasks;
    }
}
