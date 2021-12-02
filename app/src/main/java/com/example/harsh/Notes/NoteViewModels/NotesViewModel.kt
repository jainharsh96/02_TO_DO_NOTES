package com.example.harsh.Notes.NoteViewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.harsh.Notes.NoteDatabase.NotesDatabase
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteReopsitory.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private var notesRepository: NoteRepository = NoteRepository(
        NotesDatabase.getInstance(application).notesDao()
    )

    private val _note: MutableLiveData<Note> by lazy { MutableLiveData() }
    val note: LiveData<Note> = _note

    var noteId = -1

    val savedNotes: LiveData<List<Note>> = notesRepository.fetchAllNotes(Note.NOTE_STATE_SAVED)

    val draftedNotes: LiveData<List<Note>> = liveData(Dispatchers.IO) {
        notesRepository.fetchAllNotes(Note.NOTE_STATE_DRAFTED)
    }

    fun fetchNote(noteId: Int) : LiveData<Note> {
        viewModelScope.launch(Dispatchers.IO) {
            _note.postValue(notesRepository.fetchNote(noteId))
        }
        return note
    }

    //todo handle error, if write failed
    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.updateNote(note)
        }
    }

    /*
    saving deleted notes in draft state
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            note.state = Note.NOTE_STATE_DRAFTED
            notesRepository.updateNote(note)
        }
    }
}