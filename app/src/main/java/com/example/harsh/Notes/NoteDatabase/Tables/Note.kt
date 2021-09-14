package com.example.harsh.Notes.NoteDatabase.Tables

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.util.*

@Entity(tableName = "Notes")
class Note {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var body: String? = null

    @ColumnInfo(name = "updated_date")
    var date: Date? = null
    var state = 0

    @Ignore
    constructor(body: String?, date: Date?) {
        this.body = body
        this.date = date
    }

    constructor(id: Int, body: String?, date: Date?) {
        this.id = id
        this.body = body
        this.date = date
    }

    constructor() {}

    companion object {
        var NOTE_STATE_SAVED = 0
        var NOTE_STATE_DRAFTED = 1
    }
}