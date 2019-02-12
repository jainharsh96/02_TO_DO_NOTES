package com.example.harsh.Notes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = NotesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Noteslist";
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
