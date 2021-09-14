package com.example.harsh.Notes.NoteDatabase

import android.content.Context
import android.os.Environment
import androidx.room.Database
import com.example.harsh.Notes.NoteDatabase.Tables.DeletedNotes
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import net.sqlcipher.database.SupportFactory
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteUtils.DateConverter
import net.sqlcipher.database.SQLiteDatabase

@Database(entities = [Note::class, DeletedNotes::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        private const val DATABASE_NAME = "NotesDb.db"
        private val DB_FILE_PATH =
            Environment.getExternalStorageDirectory().absolutePath + "/TODONotes/"
        private const val DATABASE_PASSWORD = "thisispassword123!@#"
        private var sNotesDatabase: NotesDatabase? = null

        private val sSQLiteDatabaseHook: SQLiteDatabaseHook = object : SQLiteDatabaseHook {
            override fun preKey(database: SQLiteDatabase) {}
            override fun postKey(database: SQLiteDatabase) {
                database.rawExecSQL("PRAGMA cipher_compatibility = 3;")
                database.rawExecSQL("PRAGMA cipher_page_size = 1024;")
                database.rawExecSQL("PRAGMA kdf_iter = 64000;")
                database.rawExecSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;")
                database.rawExecSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;")
            }
        }
        val databaseSupportFactory: SupportFactory
            get() {
                val passPhrases = SQLiteDatabase.getBytes(DATABASE_PASSWORD.toCharArray())
                return SupportFactory(passPhrases, sSQLiteDatabaseHook)
            }

        fun getInstance(context: Context): NotesDatabase {
            if (sNotesDatabase == null) {
                sNotesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    DB_FILE_PATH + DATABASE_NAME
                ).openHelperFactory(databaseSupportFactory)
                    .allowMainThreadQueries()
                    .setJournalMode(JournalMode.TRUNCATE)
                    .addMigrations(object : Migration(2, 3) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL(
                                "create table if not exists 'deleted_notes' ("
                                        + "'id' INTEGER NOT NULL, " + "'updated_date' INTEGER , "
                                        + "'body' TEXT, PRIMARY KEY('id'))"
                            )
                            database.execSQL(
                                "ALTER TABLE Notes Add column state INTEGER "
                                        + "default 0 NOT NULL"
                            )
                        }
                    })
                    .build()
            }
            return sNotesDatabase!!
        }
    }
}