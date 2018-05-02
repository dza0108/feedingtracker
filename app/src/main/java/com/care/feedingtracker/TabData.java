package com.care.feedingtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TabData extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static String TAG = "TabData";

    private static EditText dateView;
    private static EditText timeView;
    private EditText foodView;
    private TextView foodTextView;

    private Button addButton;

    private static Date currentTime;
    private static DateFormat formatter;

    private static Calendar currentCalendar;


    private ArrayAdapter<CharSequence> food_adapter;

    private String diaper;

    private FeedViewModel mFeedData = null;

    private Resources res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_data, container, false);

        mFeedData = ((MainActivity)getActivity()).getmFeedViewModel();

        dateView = rootView.findViewById(R.id.date_text);
        timeView = rootView.findViewById(R.id.time_text);
        foodView = rootView.findViewById(R.id.food_text);
        foodTextView = rootView.findViewById(R.id.food_text_view);

        addButton = rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
        addButton.setFocusableInTouchMode(false);
        addButton.setFocusable(false);

        currentCalendar = Calendar.getInstance();
        currentTime = currentCalendar.getTime();

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateView.setText(formatter.format(currentTime).toString());

        Log.d(TAG, formatter.format(currentTime));

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateFrag = new DatePickerFragment();
                dateFrag.show(getFragmentManager(), "timePicker");
            }
        });

        formatter = new SimpleDateFormat("HH:mm a");
        timeView.setText(formatter.format(currentTime).toString());
        timeView.setClickable(true);
        timeView.setShowSoftInputOnFocus(false);

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFrag = new TimePickerFragment();
                timeFrag.show(getFragmentManager(), "timePicker");
            }
        });


        ArrayAdapter<CharSequence> adapter;

        Spinner spinner = rootView.findViewById(R.id.diaper_spinner);
        adapter = ArrayAdapter.createFromResource((getActivity()),
                R.array.diaper_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setTouchscreenBlocksFocus(true);
        spinner.setOnItemSelectedListener(this);

        Spinner food_spinner = rootView.findViewById(R.id.food_spinner);
        food_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.food_array, R.layout.spinner_item);

        food_adapter.setDropDownViewResource(R.layout.spinner_item);
        food_spinner.setAdapter(food_adapter);
        food_spinner.setOnItemSelectedListener(this);

        res = this.getResources();
        return rootView;
    }

    public void addFeedEntry(View view) {
        formatter = new SimpleDateFormat(res.getString(R.string.timestamp_format));

        Food food;
        if (foodView.getInputType() == InputType.TYPE_CLASS_NUMBER) {
            if (foodView.getText().toString().isEmpty()) {
                food = new Food(0);
            } else {
                food = new Food(Integer.parseInt(foodView.getText().toString()));
            }
        } else if (foodView.getInputType() == InputType.TYPE_CLASS_TEXT) {
            food = new Food(foodView.getText().toString());
        } else {
            food = new Food();
        }

        Date time = currentCalendar.getTime();
        Log.d(TAG, "time: " + formatter.format(time));
        Log.d(TAG, "food: "+ food.toString());
        Log.d(TAG, "diaper: " + diaper);

        Feed mFeed = new Feed(time.getTime(), food.getVolume(), food.getFood_name(), diaper);

        mFeedData.insert(mFeed);

        foodView.setText("");

        Toast.makeText(getActivity(), "Added ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_button:
                addFeedEntry(v);
                break;
            default:
                Log.d(TAG, "Error in get button");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Adapter a = parent.getAdapter();

        String s = a.getItem(position).toString();

        ArrayList<String> diapers =
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.diaper_array)));

        for (int i = 0; i < diapers.size(); i++) {
            if (s.matches(diapers.get(i))) {
                diaper = diapers.get(i);
                return;
            }
        }
        if (s.matches(getString(R.string.food_milk))) {
            foodView.setInputType(InputType.TYPE_CLASS_NUMBER);
            foodView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            foodView.setText("");
            foodTextView.setText(" ml");
        } else if (s.matches(getString(R.string.food_solid))) {
            foodView.setInputType(InputType.TYPE_CLASS_TEXT);
            foodView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            foodView.setText("");
            foodTextView.setText("");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "on nothing selected: ");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                return new TimePickerDialog(getActivity(), this, hour, minute,
                        true);
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(0, 0, 0, hourOfDay, minute);
            currentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            currentCalendar.set(Calendar.MINUTE, minute);
            formatter = new SimpleDateFormat("HH:mm a");
            timeView.setText(formatter.format(c.getTime()).toString());
        }

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth, 0,0);

            currentCalendar.set(Calendar.YEAR, year);
            currentCalendar.set(Calendar.MONTH, month);
            currentCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            currentCalendar.set(Calendar.DAY_OF_WEEK, 0);

            formatter = new SimpleDateFormat("dd/MM/yyyy");
            dateView.setText(formatter.format(c.getTime()).toString());
        }
    }
}
