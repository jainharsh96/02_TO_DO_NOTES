package com.example.harsh.Notes.NoteDatabase;

import com.example.harsh.Notes.NoteModels.Note;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY updated_date DESC")
    LiveData<List<Note>> loadAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserEntrie(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateEntrie(Note note);

    @Delete
    int deleteEntrie(Note note);

    @Query("SELECT * FROM notes WHERE id = :getid")
    Note loadNotesById(int getid);
}
