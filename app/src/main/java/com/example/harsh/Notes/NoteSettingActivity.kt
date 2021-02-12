package com.example.harsh.Notes

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import androidx.room.RoomDatabase.JournalMode
import com.example.harsh.Notes.NoteDatabase.NotesDatabase
import kotlinx.android.synthetic.main.activity_note_setting.*


class NoteSettingActivity : BaseActivity() {

    companion object {
        const val RESTORE_ERROR = 101
        const val RESTORE_SUCCESS = 100
    }

    val absolutePath: String = Environment.getExternalStorageDirectory().getAbsolutePath()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_setting)
        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        restore_data.setOnClickListener {
            callIntent()
        }
    }

    fun callIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                val selectedFile = data.data?.path ?: return
                val restoreTask = RestoreDataTask()
                restoreTask.execute(selectedFile)
            }
        }
    }

    inner class RestoreDataTask : AsyncTask<Any?, Any?, Any>() {
        override fun onPostExecute(o: Any) {
            super.onPostExecute(o)
            val code = o as? Int ?: return
            if (code == RESTORE_ERROR) {
                Toast.makeText(applicationContext, "Something Wrong", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Data Restored", Toast.LENGTH_LONG).show()
            }
        }

        override fun doInBackground(vararg params: Any?): Any {
            var oldDb: NotesDatabase? = null
            try {
                var path = params[0] as? String ?: throw Exception()
                path = absolutePath + "/" + path.split(":")[1]
                oldDb = Room.databaseBuilder(getApplicationContext(),
                        NotesDatabase::class.java, path)
                        .openHelperFactory(NotesDatabase.getDatabaseSupportFactory())
                        .allowMainThreadQueries()
                        .setJournalMode(JournalMode.TRUNCATE)
                        .build()
                val oldDbData = oldDb.notesDao().allData
                NotesDatabase.getInstance(applicationContext).notesDao().insertNotes(oldDbData)
            } catch (e: Exception) {
                return RESTORE_ERROR
            } finally {
                oldDb?.close()
            }
            return RESTORE_SUCCESS
        }
    }
}