package com.example.harsh.Notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harsh.Notes.NoteUtils.NotesConstants.INTENT_NOTE_ID
import com.example.harsh.Notes.NoteViewModels.MainViewModel
import com.example.harsh.Notes.NotesAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_notes_layout.*

class NotesActivity : BaseActivity(), ItemClickListener {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var mNoteViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_layout)

        action_settings.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, NoteSettingActivity::class.java)
            startActivity(intent)
        })

        fab.setOnClickListener {
            val intent = Intent(this, CreateNotesActivity::class.java)
            startActivity(intent)
        }

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this, this)
        recyclerViewTasks.adapter = notesAdapter

        setupNotesViewModel()
        setupItemTouchListener()
    }

    private fun setupItemTouchListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                mNoteViewModel.deleteNote(notesAdapter.tasks[viewHolder.adapterPosition])
                Toast.makeText(this@NotesActivity, getString(R.string.note_deleted_msg),
                        Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(recyclerViewTasks)
    }

    private fun setupNotesViewModel() {
        mNoteViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mNoteViewModel.notes.observe(this, Observer { taskEntries ->
            notesAdapter.tasks = taskEntries
            if (notesAdapter.itemCount == 0) {
                empty_view_message.visibility = View.VISIBLE
            } else {
                empty_view_message.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onItemClickListener(noteId: Int) {
        val intent = Intent(this, CreateNotesActivity::class.java)
        intent.putExtra(INTENT_NOTE_ID, noteId)
        startActivity(intent)
    }
}