package com.example.harsh.Notes.NoteDatabase

import android.content.Context
import android.os.Environment
import androidx.room.Database
import com.example.harsh.Notes.NoteDatabase.Tables.DeletedNotes
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteUtils.DateConverter
import com.example.harsh.Notes.NoteUtils.getDBMediaUri
import net.sqlcipher.database.*
import java.io.File
import java.lang.IllegalStateException

@Database(entities = [Note::class, DeletedNotes::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        const val DATABASE_NAME = "NotesDb.db"
        val DB_FILE_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" +
                    Environment.DIRECTORY_DOCUMENTS + "/"
        private const val DATABASE_PASSWORD = "thisispassword123!@#"
        private var sNotesDatabase: NotesDatabase? = null

        //todo refactore PRAGMA
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
                return SupportFactory(passPhrases, sSQLiteDatabaseHook, true)
            }

        fun getInstance(context: Context): NotesDatabase {
            if (sNotesDatabase == null) {
                sNotesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    DATABASE_NAME
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

class MySupportFactory(
    val passphrase: ByteArray, val hook: SQLiteDatabaseHook,
    val clearPassphrase: Boolean
) : SupportFactory(passphrase, hook, clearPassphrase) {
    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        return MySupportHelper(configuration, passphrase, hook, clearPassphrase)
    }
}

class MySupportHelper internal constructor(
    configuration: SupportSQLiteOpenHelper.Configuration,
    passphrase: ByteArray, hook: SQLiteDatabaseHook, clearPassphrase: Boolean
) :
    SupportSQLiteOpenHelper {
    private val standardHelper: SQLiteOpenHelper
    private val passphrase: ByteArray?
    private val clearPassphrase: Boolean
    override fun getDatabaseName(): String? {
        return standardHelper.databaseName
    }

    override fun setWriteAheadLoggingEnabled(enabled: Boolean) {
        standardHelper.setWriteAheadLoggingEnabled(enabled)
    }

    override fun getWritableDatabase(): SupportSQLiteDatabase {
        val result: SQLiteDatabase
        try {
            result = standardHelper.getWritableDatabase(passphrase)
        } catch (ex: SQLiteException) {
            if (passphrase != null) {
                var isCleared = true
                for (b: Byte in passphrase) {
                    isCleared = isCleared && b == 0.toByte()
                }
                if (isCleared) {
                    throw IllegalStateException(
                        "The passphrase appears to be cleared. This happens by" +
                                "default the first time you use the factory to open a database, so we can remove the" +
                                "cleartext passphrase from memory. If you close the database yourself, please use a" +
                                "fresh SupportFactory to reopen it. If something else (e.g., Room) closed the" +
                                "database, and you cannot control that, use SupportFactory boolean constructor option " +
                                "to opt out of the automatic password clearing step. See the project README for more information.",
                        ex
                    )
                }
            }
            throw ex
        }
        if (clearPassphrase && passphrase != null) {
            for (i in passphrase.indices) {
                passphrase[i] = 0.toByte()
            }
        }
        return result
    }

    override fun getReadableDatabase(): SupportSQLiteDatabase {
        return writableDatabase
    }

    override fun close() {
        standardHelper.close()
    }

    init {
        SQLiteDatabase.loadLibs(configuration.context)
        this.passphrase = passphrase
        this.clearPassphrase = clearPassphrase
        standardHelper = object : SQLiteOpenHelper(
            configuration.context, configuration.name,
            null, configuration.callback.version, hook
        ) {
            override fun onCreate(db: SQLiteDatabase) {
                configuration.callback.onCreate(db)
            }

            override fun onUpgrade(
                db: SQLiteDatabase, oldVersion: Int,
                newVersion: Int
            ) {
                configuration.callback.onUpgrade(
                    db, oldVersion,
                    newVersion
                )
            }

            override fun onDowngrade(
                db: SQLiteDatabase, oldVersion: Int,
                newVersion: Int
            ) {
                configuration.callback.onDowngrade(
                    db, oldVersion,
                    newVersion
                )
            }

            override fun onOpen(db: SQLiteDatabase) {
                configuration.callback.onOpen(db)
            }

            override fun onConfigure(db: SQLiteDatabase) {
                configuration.callback.onConfigure(db)
            }
        }
    }
}

