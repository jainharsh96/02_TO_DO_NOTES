package com.example.harsh.Notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY updated_date DESC")
    LiveData<List<Note>> loadAllNotes();

    @Insert
    void inserEntrie(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntrie(Note note);

    @Delete
    void deleteEntrie(Note note);

    @Query("SELECT * FROM notes WHERE id = :getid")
    LiveData<Note> loadNotesById(int getid);
}
