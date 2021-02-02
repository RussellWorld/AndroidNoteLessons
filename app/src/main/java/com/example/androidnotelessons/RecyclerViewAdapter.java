package com.example.androidnotelessons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Note> dataSource;
    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        return new ViewHolder(v);
    }

    public RecyclerViewAdapter(ArrayList<Note> dataSource) {
        this.dataSource = dataSource;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.note_name_text_view);
            textViewDate = itemView.findViewById(R.id.note_date_text_view);
            itemView.setOnClickListener(v -> {
                clickListener.onItemClick(getAdapterPosition());
            });
        }

        public void onBind(Note note) {
            textViewName.setText(note.getName());
            textViewDate.setText(note.getFormatedCreationDate());
        }
    }
}