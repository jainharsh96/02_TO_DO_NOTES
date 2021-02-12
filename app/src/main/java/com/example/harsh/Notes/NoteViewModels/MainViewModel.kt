package com.example.harsh.Notes.NoteViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.harsh.Notes.NoteDatabase.NotesDatabase
import com.example.harsh.Notes.NoteModels.Note
import com.example.harsh.Notes.NoteReopsitory.NoteReopsitory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mNoteRepository: NoteReopsitory = NoteReopsitory(
            NotesDatabase.getInstance(application).notesDao())

    private var mDisposable: CompositeDisposable = CompositeDisposable()

    var note: MutableLiveData<Note> = MutableLiveData()

    var notes: LiveData<List<Note>> = mNoteRepository.getAllNotes("")

//    fun loadNotes(sortBy: String) {
//        mNoteRepository.getAllNotes(sortBy)
//    }

    fun loadNote(noteId: Int) {
        mDisposable.add(mNoteRepository.getNote(noteId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { note.value = it })
    }

    fun insertNote(note: Note) {
        mDisposable.add(mNoteRepository.insertNote(note).subscribeOn(Schedulers.io()).observeOn
        (AndroidSchedulers.mainThread()).subscribe({}, {}))
    }

    fun updateNote(note: Note) {
        mDisposable.add(mNoteRepository.updateNote(note).subscribeOn(Schedulers.io()).observeOn
        (AndroidSchedulers.mainThread()).subscribe({}, {}))
    }

    fun deleteNote(note: Note) {
        mDisposable.add(mNoteRepository.deleteNote(note).subscribeOn(Schedulers.io()).observeOn
        (AndroidSchedulers.mainThread()).subscribe({}, {}))
    }

    fun clear() {
        mDisposable.dispose()
    }
}