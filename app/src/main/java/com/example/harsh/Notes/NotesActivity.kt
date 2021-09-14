package com.example.harsh.Notes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteUtils.INTENT_NOTE_ID
import com.example.harsh.Notes.NoteViewModels.NotesViewModel
import com.example.harsh.Notes.NotesAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_notes_layout.*
import java.util.*

open class NotesActivity : BaseActivity(), ItemClickListener {

    companion object {
        private val TAG = "NotesActivity"
    }

    val notesAdapter: NotesAdapter by lazy { NotesAdapter(this) }
    val mNoteViewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }
    val notesObserver = Observer<List<Note>> {
        notesAdapter.notes = it.toMutableList()
        if (it.isEmpty()) {
            empty_view_message.makeVisible()
        } else {
            empty_view_message.makeGone()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissionForAudio()
            } else {
                startRecognizeVoice()
            }
        }
        setupNotesObserver()
        setupNotesRecyclerView()
    }

    open fun setupBackPressButton() {
        setSupportActionBar(toolbar1)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setupNotesRecyclerView() {
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = notesAdapter
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                onSwipeNote(notesAdapter.notes[viewHolder.adapterPosition])
            }
        }).attachToRecyclerView(recyclerViewTasks)
    }

    open fun setupNotesObserver() {
        mNoteViewModel.savedNotes.observe(this, notesObserver)
    }

    open fun onSwipeNote(note: Note) {
        mNoteViewModel.deleteNote(note)
        showToast("Note Drafted")
    }

    override fun onItemClickListener(noteId: Int) {
        val intent = Intent(this, CreateNotesActivity::class.java)
        intent.putExtra(INTENT_NOTE_ID, noteId)
        startActivity(intent)
    }

    private fun saveNote(text: String) {
        mNoteViewModel.insertNote(Note(text, Date()))
    }

    override fun onRecognizeVoiceText(texts: ArrayList<String?>?) {
        val searchText = StringBuilder()
        texts!!.forEach { searchText.append(it).append(" ") }
        saveNote(searchText.toString())
    }
}