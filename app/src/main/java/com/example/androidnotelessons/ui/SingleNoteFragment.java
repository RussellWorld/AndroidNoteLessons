package com.example.androidnotelessons.ui;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.androidnotelessons.FragmentHandler;
import com.example.androidnotelessons.MainActivity;
import com.example.androidnotelessons.R;
import com.example.androidnotelessons.data.Note;
import com.example.androidnotelessons.observe.Publisher;

import java.util.Calendar;

public class SingleNoteFragment extends Fragment {

    static final String ARG_SINGLE_NOTE = "note";
    private Publisher publisher;
    private Note note;
    private Calendar creationDate = Calendar.getInstance();
    EditText etName;
    TextView tvDate;
    EditText etDescription;
    EditText etContent;
    Button buttonOk;
    Button buttonCancel;

    public SingleNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) requireContext();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
        creationDate.set(year, month, dayOfMonth);
        setDateTextView(creationDate.getTimeInMillis());
    };

    public static SingleNoteFragment newInstance(Note note) {
        SingleNoteFragment fragment = new SingleNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SINGLE_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static SingleNoteFragment newInstance() {
        SingleNoteFragment fragment = new SingleNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_SINGLE_NOTE);
            creationDate.setTimeInMillis(note.getCreationDateUnixTime());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_note, container, false);
        setHasOptionsMenu(true);

        initView(view);
        if (note != null) {
            populateView();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //publisher.unsubscribeAll();
    }

    private Note collectNote() {
        String name = this.etName.getText().toString();
        String description = this.etDescription.getText().toString();
        Boolean isImportant = true;
        String content = this.etContent.getText().toString();
        if (note != null){
            Note answer = new Note(name, description, creationDate.getTimeInMillis(), isImportant, content);
            answer.setId(note.getId());
            return answer;
        }else {
            return new Note(name, description, creationDate.getTimeInMillis(), isImportant, content);
        }
    }

    private void initView(View view) {
        etName = view.findViewById(R.id.edit_text_name);
        tvDate = view.findViewById(R.id.text_view_date);

        etDescription = view.findViewById(R.id.edit_text_description);
        etContent = view.findViewById(R.id.edit_text_content);
        tvDate.setOnClickListener(v -> {
            new DatePickerDialog(requireActivity(), onDateSetListener, creationDate.get(Calendar.YEAR),
                    creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        //Пока оба листенера будут делать одно и тоже. Разветвлю логику,
        // когда буду реализовывать сохранение измененных заметок
        buttonOk = view.findViewById(R.id.button_ok_single_note);
        buttonOk.setOnClickListener(v -> {
                    note = collectNote();
                    publisher.notifySingle(note);
                    popBackStackIfNotLand();
                }
        );
        buttonCancel = view.findViewById(R.id.button_cancel_single_note);
        buttonCancel.setOnClickListener(v -> popBackStackIfNotLand());
    }

    private void populateView() {
        etName.setText(note.getName());
        setDateTextView(note.getCreationDateUnixTime());
        etDescription.setText(note.getDescription());
        etContent.setText(note.getContent());
    }

    private void setDateTextView(long dateUT) {
        tvDate.setText(DateUtils.formatDateTime(null, dateUT, DateUtils.FORMAT_SHOW_DATE));
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