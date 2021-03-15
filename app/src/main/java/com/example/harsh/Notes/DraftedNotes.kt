package com.example.harsh.Notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NoteModels.Note
import com.example.harsh.Notes.NoteViewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_notes_layout.*

class DraftedNotes : NotesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notes_title.text = "Drafted Notes"
        fab.visibility = View.GONE
        fab_voice_note.visibility = View.GONE
        action_settings.visibility = View.GONE
    }

    override fun setupBackPressButton() {
        setSupportActionBar(toolbar1)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun setupItemTouchListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val note = notesAdapter.tasks[viewHolder.adapterPosition]
                note.state = Note.NOTE_STATE_SAVED
                mNoteViewModel.updateNote(note)
                Toast.makeText(this@DraftedNotes, "Note Restored", Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(recyclerViewTasks)
    }

    override fun setupNotesViewModel() {
        mNoteViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mNoteViewModel.loadNotes(Note.NOTE_STATE_DRAFTED).observe(this, notesObserver)
    }

    override fun onItemClickListener(noteId: Int) {

    }
}