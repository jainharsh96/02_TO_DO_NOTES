package com.example.harsh.Notes.NoteReopsitory

import com.example.harsh.Notes.NoteDatabase.NotesDao
import com.example.harsh.Notes.NoteModels.Note
import io.reactivex.Completable
import io.reactivex.Observable

class NoteReopsitory(private val dao: NotesDao) {

    fun getAllNotes(state: Int) = dao.loadAllNotes(state)

    fun getNote(noteId: Int): Observable<Note> = Observable.fromCallable {
        dao.loadNotesById(noteId)
    }

    fun insertNote(note: Note) = Completable.create {
        val flag = dao.inserEntrie(note)
        if (flag > 0) {
            it.onComplete()
        } else {
            it.onError(Throwable())
        }
    }

    fun updateNote(note: Note) = Completable.create {
        val flag = dao.updateEntrie(note)
        if (flag > 0) {
            it.onComplete()
        } else {
            dao.inserEntrie(note)
        }
    }

    fun deleteNote(note: Note) = Completable.create {
        val flag = dao.deleteEntrie(note)
        if (flag > 0) {
            dao.insertDeletedNotes(note)
            it.onComplete()
        } else {
            it.onError(Throwable())
        }
    }
}