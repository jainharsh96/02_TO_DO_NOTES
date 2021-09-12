package com.example.harsh.Notes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NoteModels.Note
import com.example.harsh.Notes.NoteUtils.NotesConstants.INTENT_NOTE_ID
import com.example.harsh.Notes.NoteViewModels.MainViewModel
import com.example.harsh.Notes.NotesAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_notes_layout.*
import java.util.*

open class NotesActivity : BaseActivity(), ItemClickListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val RESULT_SPEECH = 1234
    }

    lateinit var notesAdapter: NotesAdapter
    lateinit var mNoteViewModel: MainViewModel

    val notesObserver = Observer<List<Note>> {
        notesAdapter.tasks = it
        if (notesAdapter.itemCount == 0) {
            empty_view_message.visibility = View.VISIBLE
        } else {
            empty_view_message.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: " )
        setContentView(R.layout.activity_notes_layout)
        setupBackPressButton()
        action_settings.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, NoteSettingActivity::class.java)
            startActivity(intent)
        })

        fab.setOnClickListener {
            val intent = Intent(this, CreateNotesActivity::class.java)
            startActivity(intent)
        }

        fab_voice_note.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                checkPermissionForAudion()
            } else {
                startRecognizeVoice()
            }
        }

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this, this)
        recyclerViewTasks.adapter = notesAdapter

        setupNotesViewModel()
        setupItemTouchListener()
    }

    open fun setupBackPressButton() {
        setSupportActionBar(toolbar1)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    open fun setupItemTouchListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                mNoteViewModel.deleteNote(notesAdapter.tasks[viewHolder.adapterPosition])
                Toast.makeText(this@NotesActivity, "Note Drafted",
                        Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(recyclerViewTasks)
    }

    open fun setupNotesViewModel() {
        mNoteViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mNoteViewModel.loadNotes(Note.NOTE_STATE_SAVED).observe(this, notesObserver)
    }

    override fun onItemClickListener(noteId: Int) {
        val intent = Intent(this, CreateNotesActivity::class.java)
        intent.putExtra(INTENT_NOTE_ID, noteId)
        startActivity(intent)
    }

    private fun saveNote(text: String) {
        mNoteViewModel.insertNote(Note(text, Date()))
    }

    override fun onRecognizeVoiceText(texts: ArrayList<String>) {
        val searchText = StringBuilder()
        texts.forEach { searchText.append(it).append(" ") }
        saveNote(searchText.toString())
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.e(MainActivity.TAG, "onRestoreInstanceState: ")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.e(MainActivity.TAG, "onPostCreate: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(MainActivity.TAG, "onRestart: ")
    }

    override fun onStart() {
        super.onStart()
        Log.e(MainActivity.TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e(MainActivity.TAG, "onResume: ")
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.e(MainActivity.TAG, "onPostResume: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e(MainActivity.TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(MainActivity.TAG, "onDestroy: ")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e(MainActivity.TAG, "onSaveInstanceState: ")
    }
}