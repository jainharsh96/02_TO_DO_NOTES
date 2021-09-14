package com.example.harsh.Notes

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NotesAdapter.TaskViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.harsh.Notes.NoteDatabase.Tables.Note
import com.example.harsh.Notes.NoteUtils.NOTE_DATE_FORMAT
import kotlinx.android.synthetic.main.cardview_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(private val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<TaskViewHolder>() {

    var notes = mutableListOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val dateFormater by lazy { SimpleDateFormat(NOTE_DATE_FORMAT, Locale.getDefault()) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val note = notes[position]
        val body = note.body
        val date = dateFormater.format(note.date)
        holder.itemview.body.text = body
        holder.itemview.title.text = body!!.split("\n".toRegex()).toTypedArray()[0]
        holder.itemview.date.text = date
        holder.itemview.setOnClickListener {
            mItemClickListener.onItemClickListener(note.id)
        }
    }

    override fun getItemCount() = notes.size

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    class TaskViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview)
}