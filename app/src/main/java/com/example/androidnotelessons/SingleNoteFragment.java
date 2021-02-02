package com.example.androidnotelessons;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class SingleNoteFragment extends Fragment {

    static final String ARG_SINGLE_NOTE = "note";

    private Note note;

    public SingleNoteFragment() {
        // Required empty public constructor
    }

    public static SingleNoteFragment newInstance(Note note) {
        SingleNoteFragment fragment = new SingleNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SINGLE_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_SINGLE_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_note, container, false);
        TextView tvName = view.findViewById(R.id.text_view_name);
        tvName.setText(note.getName());
        TextView tvDate = view.findViewById(R.id.text_view_date);
        tvDate.setText(note.getFormatedCreationDate());
        tvDate.setOnClickListener(v -> {
            DataPickerFragment detail = DataPickerFragment.newInstance(note);
            FragmentActivity activity = requireActivity();
            FragmentHandler.replaceFragment(activity, detail, FragmentHandler.getIdFromOrientation(activity), true);

        });
        Button buttonOk = view.findViewById(R.id.button_ok_single_note);
        //Пока оба листенера будут делать одно и тоже. Разветвлю логику,
        // когда буду реализовывать сохранение измененных заметок
        buttonOk.setOnClickListener(v -> popBackStackIfNotLand());
        Button buttonCancel = view.findViewById(R.id.button_cancel_single_note);
        buttonCancel.setOnClickListener(v -> popBackStackIfNotLand());

        TextView tvDescription = view.findViewById(R.id.text_view_description);
        tvDescription.setText(note.getDescription());
        TextView tvContent = view.findViewById(R.id.edit_text_content);
        tvContent.setText(note.getContent());
        setHasOptionsMenu(true);
        return view;
    }

    //Выкидываем активность из стека, когда ориентация портретная
    private void popBackStackIfNotLand() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            FragmentHandler.popBackStack(requireActivity());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_single_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.send_note:
                Toast.makeText(getContext(), "Send chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.add_photo:
                Toast.makeText(getContext(), "Add photo chosen", Toast.LENGTH_LONG).show();
                return true;
            case R.id.add_link:
                Toast.makeText(getContext(), "Add link chosen", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}