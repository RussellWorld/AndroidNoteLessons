package com.example.androidnotelessons.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnotelessons.R;
import com.example.androidnotelessons.data.Note;
import com.example.androidnotelessons.data.NoteSource;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private NoteSource dataSource;
    private OnItemClickListener clickListener;
    private final Fragment fragment;
    private int menuPosition;

    public RecyclerViewAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        return new ViewHolder(v);
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setDataSource(NoteSource noteSource) {
        this.dataSource = noteSource;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dataSource.getNote(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.note_name_text_view);
            textViewDate = itemView.findViewById(R.id.note_date_text_view);
            registerContextMenu(itemView);
            itemView.setOnClickListener(v -> clickListener.onItemClick(getAdapterPosition()));
        }

        public void onBind(Note note) {
            textViewName.setText(note.getName());
            textViewDate.setText(note.getFormatedCreationDate());
        }

        private void registerContextMenu(View view) {
            if (fragment != null) {
                view.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(view);
            }
        }
    }
}