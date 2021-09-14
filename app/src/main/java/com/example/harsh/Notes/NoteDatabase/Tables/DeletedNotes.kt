package com.example.harsh.Notes.NoteDatabase.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "deleted_notes")
class DeletedNotes {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var body: String? = null

    @ColumnInfo(name = "updated_date")
    var date: Date? = null

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

}