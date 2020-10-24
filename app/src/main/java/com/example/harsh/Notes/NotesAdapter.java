package com.example.harsh.Notes;

import com.example.harsh.Notes.NoteModels.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.TaskViewHolder> {
    private static final String DATE_FORMAT = "dd-MM-yyy";
    final private ItemClickListener mItemClickListener;
    private List<Note> mNotes;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public NotesAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.cardview_layout, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Note tableEntry = mNotes.get(position);
        String body = tableEntry.getBody();
        String date = dateFormat.format(tableEntry.getDate());
        holder.rBody.setText(body);
        holder.rTitle.setText(body.split("\n")[0]);
        holder.rDate.setText(date);
    }

    @Override
    public int getItemCount() {
        if (mNotes == null) {
            return 0;
        }
        return mNotes.size();
    }

    public void setTasks(List<Note> tableEntries) {
        mNotes = tableEntries;
        notifyDataSetChanged();
    }

    public List<Note> getTasks() {
        return mNotes;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rTitle;
        TextView rDate;
        TextView rBody;

        public TaskViewHolder(View itemview) {
            super(itemview);
            rTitle = itemview.findViewById(R.id.title);
            rDate = itemview.findViewById(R.id.date);
            rBody = itemview.findViewById(R.id.body);
            itemview.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int elementId = mNotes.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
