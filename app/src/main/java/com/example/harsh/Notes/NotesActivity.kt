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
import com.example.harsh.Notes.NoteDatabase.NotesDatabase
import com.example.harsh.Notes.NoteViewModels.MainViewModel
import com.example.harsh.Notes.NotesAdapter.ItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_notes_layout.*

class NotesActivity : BaseActivity(), ItemClickListener {
    private val TAG = MainActivity::class.java.simpleName
    private var notesAdapter: NotesAdapter? = null
    private var dbs: NotesDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_layout)
        action_settings.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, NoteSettingActivity::class.java)
            startActivity(intent)
        })
        recyclerViewTasks.setLayoutManager(LinearLayoutManager(this))
        notesAdapter = NotesAdapter(this, this)
        recyclerViewTasks.setAdapter(notesAdapter)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                AppExecuter.getInstance().diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val tasks = notesAdapter!!.tasks
                    dbs!!.notesDao().deleteEntrie(tasks[position])
                }
                Toast.makeText(applicationContext, getString(R.string.note_deleted_msg), Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(recyclerViewTasks)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val intent = Intent(this, CreateNotesActivity::class.java)
            startActivity(intent)
        }
        dbs = NotesDatabase.getInstance(applicationContext)
        setupViewModel()
    }

    private fun setupViewModel() {
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.tasks.observe(this, Observer { taskEntries ->
            notesAdapter!!.tasks = taskEntries
            if (notesAdapter!!.itemCount == 0) {
                empty_view_message!!.visibility = View.VISIBLE
            } else {
                empty_view_message!!.visibility = View.INVISIBLE
            }
        })
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this, CreateNotesActivity::class.java)
        intent.putExtra(CreateNotesActivity.KEY, itemId)
        startActivity(intent)
    }
}