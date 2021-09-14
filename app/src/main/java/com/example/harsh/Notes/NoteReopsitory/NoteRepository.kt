package com.example.harsh.Notes.NoteReopsitory

import com.example.harsh.Notes.NoteDatabase.NotesDao
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import io.reactivex.Completable
import io.reactivex.Observable

class NoteRepository(private val localDbClient: NotesDao) {

    fun fetchAllNotes(state: Int) = localDbClient.fetchAllNotes(state)

    suspend fun fetchNote(noteId: Int) = localDbClient.findNoteById(noteId)

    suspend fun insertNote(note: Note) {
        val flag = localDbClient.insertNote(note)
//        if (flag > 0) {
//            it.onComplete()
//        } else {
//            it.onError(Throwable())
//        }
    }

    suspend fun updateNote(note: Note) {
        val flag = localDbClient.updateNote(note)
        if (flag > 0) {
            // it.onComplete()
        } else {
            localDbClient.insertNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        val flag = localDbClient.deleteNote(note)
        if (flag > 0) {
            localDbClient.insertDeletedNotes(note)
            // it.onComplete()
        } else {
            // it.onError(Throwable())
        }
    }
}