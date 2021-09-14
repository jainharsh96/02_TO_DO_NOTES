package com.example.harsh.Notes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteUtils.INTENT_NOTE_ID
import com.example.harsh.Notes.NoteViewModels.NotesViewModel
import kotlinx.android.synthetic.main.create_notes_layout.*
import java.util.*

class CreateNotesActivity : BaseActivity() {
    private val mNoteViewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }
    var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_notes_layout)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        Notes_data.addTextChangedListener(textWatcher)
        revert.setOnClickListener {
            if (mNoteViewModel.note.value != null) {
                Notes_data.setText(mNoteViewModel.note.value!!.body)
            }
        }

        voice_note.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissionForAudio()
            } else {
                startRecognizeVoice()
            }
        }

        if (intent != null) {
            noteId = intent.getIntExtra(INTENT_NOTE_ID, -1)
        }
        setupNoteViewModel()
    }

    private fun setupNoteViewModel() {
        mNoteViewModel.note.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                populateUI(it)
            }
        })
        if (noteId > 0) {
            mNoteViewModel.fetchNote(noteId)
        }
    }

    fun SaveData(view: View?) {
        val body = Notes_data.text.toString()
        val date = Date()
        val newNote = Note(body, date)
        if (noteId <= 0) {
            mNoteViewModel.insertNote(newNote)
            finish()
        } else {
            newNote.id = noteId
            mNoteViewModel.updateNote(newNote)
            finish()
        }
    }

    private fun populateUI(note: Note) {
        Notes_data.setText(note.body)
        NoteTitle.text = getString(R.string.edit_note_title)
        hideKeyboard()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            Notes_save.isEnabled = false
            Notes_save.setBackgroundColor(resources.getColor(R.color.disable))
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Notes_save.isEnabled = true
            Notes_save.setBackgroundColor(resources.getColor(R.color.colorUpdate))
        }

        override fun afterTextChanged(s: Editable) {
            val body = Notes_data.text.toString().trim { it <= ' ' }
            if (body.isEmpty()) {
                Notes_save.isEnabled = false
                Notes_save.setBackgroundColor(resources.getColor(R.color.disable))
            } else {
                Notes_save.isEnabled = true
                Notes_save.setBackgroundColor(resources.getColor(R.color.colorUpdate))
            }
            if (noteId > 0) {
                revert.visibility = View.VISIBLE
            }
        }
    }

    override fun onRecognizeVoiceText(texts: ArrayList<String?>?) {
        val searchText = StringBuilder()
        texts!!.forEach { searchText.append(it).append(" ") }
        Notes_data.append(searchText.toString())
    }
}