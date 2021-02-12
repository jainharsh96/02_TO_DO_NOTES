package com.example.harsh.Notes

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import com.example.harsh.Notes.NoteModels.Note
import com.example.harsh.Notes.NoteUtils.NotesConstants
import com.example.harsh.Notes.NoteViewModels.MainViewModel
import kotlinx.android.synthetic.main.create_notes_layout.*
import java.util.*

class CreateNotesActivity : BaseActivity() {
    private lateinit var mNoteViewModel: MainViewModel
    var DEFAULT_KEY = -1
    var mNoteId = DEFAULT_KEY
    var mNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_notes_layout)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        Notes_data.addTextChangedListener(textWatcher)
        revert.setOnClickListener {
            if (mNote != null) {
                Notes_data.setText(mNote!!.body)
            }
        }
        if (intent != null) {
            mNoteId = intent.getIntExtra(NotesConstants.INTENT_NOTE_ID, DEFAULT_KEY)
        }
        initViewModel()
    }

    fun initViewModel() {
        mNoteViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mNoteViewModel.note.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                mNote = it
                populateUI(it)
            }
        })
        if (mNoteViewModel.note.value == null && mNoteId != DEFAULT_KEY) {
            mNoteViewModel.loadNote(mNoteId)
        }
    }

    fun SaveData(view: View?) {
        val body = Notes_data.text.toString()
        val date = Date()
        val record = Note(body, date)
        if (mNoteId == DEFAULT_KEY) {
            mNoteViewModel.insertNote(record)
            finish()
        } else {
            record.id = mNoteId
            mNoteViewModel.updateNote(record)
            finish()
        }
    }

    fun populateUI(note: Note) {
        Notes_data.setText(note.body)
        NoteTitle.text = getString(R.string.edit_note_title)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(Notes_data.windowToken, 0)
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
            if (mNoteId != DEFAULT_KEY) {
                revert.visibility = View.VISIBLE
            }
        }
    }
}