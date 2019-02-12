package com.example.harsh.Notes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

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
