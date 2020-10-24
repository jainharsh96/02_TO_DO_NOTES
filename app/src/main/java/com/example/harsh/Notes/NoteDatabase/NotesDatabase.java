package com.example.harsh.Notes.NoteDatabase;

import com.example.harsh.Notes.NoteModels.Note;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = NotesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "NotesDb";
    private static NotesDatabase sNotesDatabase;

    public static NotesDatabase getInstance(Context context){
      if(sNotesDatabase == null){
        synchronized (LOCK){
            sNotesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDatabase.class,
                    NotesDatabase.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
      }
      return sNotesDatabase;
    }
    public abstract NotesDao notesDao();
}
