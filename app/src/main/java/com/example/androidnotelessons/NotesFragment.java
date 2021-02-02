package com.example.androidnotelessons;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private static final String CURRENT_NOTE = "CurrentNote";
    private int currentNoteInt = 0;
    private ArrayList<Note> notesArray = new ArrayList<>();
    private boolean isLandscape;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNotesArray();
        initList(view);
    }

    private void setNotesArray() {
        if (notesArray.size() > 0) {
            notesArray.clear();
        }
        String[] notes = getResources().getStringArray(R.array.names);
        for (int i = 0; i < notes.length; i++) {
            notesArray.add(createNewNote(i));
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Установим признак ландшафтной ориентации
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNoteInt = savedInstanceState.getInt(CURRENT_NOTE);
        } else {
            currentNoteInt = 0;
        }

        if (isLandscape) {
            showNoteLand(getNote(currentNoteInt));
        }
    }

    //Инициализируем интерфейс
    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(notesArray);
        recyclerViewAdapter.setOnItemClickListener(position -> showNote(getNote(position)));
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private Note getNote(int position) {
        return notesArray.get(position);
    }

    private Note createNewNote(int index) {
        Resources res = getResources();
        int isImportantInt = Integer.parseInt(res.getStringArray(R.array.importances)[index]);
        Boolean isImportant = isImportantInt == 1;
        Note note = new Note(res.getStringArray(R.array.names)[index],
                res.getStringArray(R.array.descriptions)[index], Long.parseLong(res.getStringArray(R.array.datesUT)[index]), isImportant, res.getStringArray(R.array.contents)[index]);
        return note;
    }

    //Покажем содержимое заметки
    private void showNote(Note currentNote) {
        if (isLandscape) {
            showNoteLand(currentNote);
        } else {
            showNotePort(currentNote);
        }
    }

    //Покажем содержимое заметки для ландшафтного режима
    private void showNoteLand(Note currentNote) {
        SingleNoteFragment detail = SingleNoteFragment.newInstance(currentNote);
        FragmentHandler.replaceFragment(requireActivity(), detail, R.id.single_note, false);
    }

    //Покажем содержимое заметки для портретного режима
    private void showNotePort(Note currentNote) {
        Context context = getContext();
        if (context != null) {
            SingleNoteFragment detail = SingleNoteFragment.newInstance(currentNote);
            FragmentHandler.replaceFragment(requireActivity(), detail, R.id.notes, true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_NOTE, currentNoteInt);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_note:
                Toast.makeText(getContext(), "Add chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort_notes:
                Toast.makeText(getContext(), "Sort chosen", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}