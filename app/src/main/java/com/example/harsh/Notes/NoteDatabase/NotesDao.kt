package com.example.harsh.Notes.NoteDatabase

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.harsh.Notes.NoteDatabase.Tables.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes where state = :state ORDER BY updated_date DESC")
    fun fetchAllNotes(state: Int): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<Note>): List<Long>?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note): Int

    @Delete
    suspend fun deleteNote(note: Note): Int

    @Query("SELECT * FROM notes WHERE id = :getid")
    suspend fun findNoteById(getid: Int): Note?

    @Query("SELECT * FROM notes where state = 0")
    fun getAllSavedNotes(): List<Note>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedNotes(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotesMain(notes: List<Note>): List<Long>?
}