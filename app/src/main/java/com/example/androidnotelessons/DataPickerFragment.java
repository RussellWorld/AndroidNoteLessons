package com.example.androidnotelessons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataPickerFragment extends Fragment {
    private static final String ARG_NOTE = "note";
    private Note note;

    public DataPickerFragment() {
        // Required empty public constructor
    }

    public static DataPickerFragment newInstance(Note note) {
        DataPickerFragment fragment = new DataPickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        initDatePicker(view);
        initButtons(view);
    }

    //Инициализируем кнопки
    private void initButtons(View view) {
        Button buttonOk = view.findViewById(R.id.button_ok_date_picker);
        buttonOk.setOnClickListener((View.OnClickListener) v -> {
            DatePicker picker = (DatePicker) view.findViewById(R.id.date_picker);
            Calendar calendar;
            calendar = new GregorianCalendar(picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
            long dateUT = calendar.getTimeInMillis();
            note.setCreationDateUnixTime(dateUT);
            FragmentHandler.popBackStack(requireActivity());
        });
        Button buttonCancel = view.findViewById(R.id.button_cancel_date_picker);
        buttonCancel.setOnClickListener(v -> FragmentHandler.popBackStack(requireActivity()));
    }

    //Инициализируем DatePicker
    private void initDatePicker(View view) {
        DatePicker picker = (DatePicker) view.findViewById(R.id.date_picker);
        long dateUT = note.getCreationDateUnixTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) dateUT));
        picker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_picker, container, false);
    }
}