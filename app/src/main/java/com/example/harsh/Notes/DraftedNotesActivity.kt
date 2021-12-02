package com.example.harsh.Notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteViewModels.NotesViewModel
import kotlinx.android.synthetic.main.activity_notes_layout.*

class DraftedNotesActivity : NotesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notes_title.text = "Drafted Notes"
        fab.visibility = View.GONE
        fab_voice_note.visibility = View.GONE
        action_settings.visibility = View.GONE
    }

    override fun setupBackPressButton() {
        setSupportActionBar(toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSwipeNote(note: Note) {
        mNoteViewModel.deleteNote(note)
        showToast("Note Restored to original place")
    }

    override fun setupNotesObserver() {
        mNoteViewModel.draftedNotes.observe(this, notesObserver)
    }
}