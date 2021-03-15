package com.example.harsh.Notes.NoteModels;


import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class Note {

    public static int NOTE_STATE_SAVED = 0;
    public static int NOTE_STATE_DRAFTED = 1;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String body;
    @ColumnInfo(name = "updated_date")
    private Date date;
    private int state;

    @Ignore
    public Note(String body, Date date) {
        this.body = body;
        this.date = date;
    }

    public Note(int id, String body, Date date) {
        this.id = id;
        this.body = body;
        this.date = date;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
