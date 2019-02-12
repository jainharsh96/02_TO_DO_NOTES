package com.example.harsh.Notes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mNoNotesMessage;
    private NotesAdapter notesAdapter;
    private NotesDatabase dbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mNoNotesMessage = findViewById(R.id.empty_view_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, this);
        mRecyclerView.setAdapter(notesAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Note> tasks = notesAdapter.getTasks();
                        dbs.notesDao().deleteEntrie(tasks.get(position));

                    }
                });
                Toast.makeText(getApplicationContext(), getString(R.string.note_deleted_msg), Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNotesActivity.class);
                startActivity(intent);
            }
        });

        dbs = NotesDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> taskEntries) {
                notesAdapter.setTasks(taskEntries);
                if (notesAdapter.getItemCount() == 0) {
                    mNoNotesMessage.setVisibility(View.VISIBLE);
                } else {
                    mNoNotesMessage.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, CreateNotesActivity.class);
        intent.putExtra(CreateNotesActivity.KEY, itemId);
        startActivity(intent);
    }
}