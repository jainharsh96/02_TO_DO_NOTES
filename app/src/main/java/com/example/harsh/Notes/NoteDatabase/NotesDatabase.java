package com.example.harsh.Notes.NoteDatabase;

import com.example.harsh.Notes.NoteModels.Note;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SupportFactory;

import android.content.Context;
import android.os.Environment;

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
    private static final String DATABASE_NAME_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/HarshNotes/NotesDb";
    private static final String DATABASE_PASSWORD = "thisispassword123!@#";
    private static NotesDatabase sNotesDatabase;

    private static SQLiteDatabaseHook sSQLiteDatabaseHook = new SQLiteDatabaseHook() {
        @Override
        public void preKey(SQLiteDatabase database) {

        }

        @Override
        public void postKey(SQLiteDatabase database) {
            database.rawExecSQL("PRAGMA cipher_compatibility = 3;");
            database.rawExecSQL("PRAGMA cipher_page_size = 1024;");
            database.rawExecSQL("PRAGMA kdf_iter = 64000;");
            database.rawExecSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;");
            database.rawExecSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;");
        }
    };


    private static SupportFactory getDatabaseSupportFactory() {
        byte[] passPhrases = SQLiteDatabase.getBytes(DATABASE_PASSWORD.toCharArray());
        return new SupportFactory(passPhrases, sSQLiteDatabaseHook);
    }

    public static NotesDatabase getInstance(Context context) {
        if (sNotesDatabase == null) {
            synchronized (LOCK) {
                sNotesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        NotesDatabase.class,
                        NotesDatabase.DATABASE_NAME_PATH)
                        .openHelperFactory(getDatabaseSupportFactory())
                        .setJournalMode(JournalMode.TRUNCATE)
                    .build();
        }
      }
      return sNotesDatabase;
    }
    public abstract NotesDao notesDao();
}
