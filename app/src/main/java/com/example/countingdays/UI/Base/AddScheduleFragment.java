package com.example.countingdays.UI.Base;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.R;
import com.example.countingdays.Utils.AppConstant;
import com.example.countingdays.Utils.Utils;
import com.example.countingdays.ViewModels.AddScheduleViewModel;
import com.example.countingdays.databinding.FragmentAddScheduleBinding;
import com.google.android.gms.ads.AdRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddScheduleFragment extends Fragment implements View.OnClickListener {

    FragmentAddScheduleBinding binding;
    private final Calendar calendar = Calendar.getInstance();
    AddScheduleViewModel addScheduleViewModel;
    String colorValue = AppConstant.COLOR_BLUE;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public int h = 0,m = 0,y = 0,mon = 0,d = 0 ;

    String selectedDateAndTime = null;

    public AddScheduleFragment() {
        // Required empty public constructor
    }

    public static AddScheduleFragment newInstance(String param1, String param2) {
        AddScheduleFragment fragment = new AddScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void sendToHomeFragment(){

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }


    public void setListeners(){

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendToHomeFragment();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scheduleName = binding.title.getText().toString();
                String scheduleDateTime = selectedDateAndTime;

                if(ValidData(scheduleName,scheduleDateTime)){

                    LocalDateTime calculateTime = LocalDateTime.parse(scheduleDateTime);
                    long hoursCalculate = ChronoUnit.HOURS.between(LocalDateTime.now(),calculateTime);

                    String startTime = LocalDateTime.now().toString();



                    Log.d("--duration", "onClick: "+startTime);

                    Schedule schedule = new Schedule(scheduleName,scheduleDateTime,colorValue,startTime);
                    addScheduleViewModel.insertNewSchedule(schedule);
                    sendToHomeFragment();
                }
                else{
                    Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }


            }
        });





        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cl = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(),R.style.DateTimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        y = year;
                        mon = month;
                        d = day;

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        binding.dateTxt.setText(DateFormatUtils.format(calendar.getTime(), AppConstant.FORMAT_ADD_CLAIM_DATE));
                        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        inputParser.setTimeZone(TimeZone.getTimeZone("UTC"));
                        final String format = inputParser.format(calendar.getTime());


                    }
                }, cl.get(Calendar.YEAR),cl.get(Calendar.MONTH),cl.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                //dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });


        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        binding.time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.DateTimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                h = i;
                                m = i1;

                                if(mon != 12) {
                                    mon++;
                                }

                                String sss = y+"-"+mon+"-"+d+" "+h+":"+m+":"+"00";

                                // convert to Date string
                                 selectedDateAndTime = LocalDateTime.of(y, mon, d, h, m).toString();

                                // convert to string to Date
                                LocalDateTime time = LocalDateTime.parse(selectedDateAndTime);
                                Log.d("--getDates", "onTimeSet: "+time);

                                String prceedeHour = h < 10 ? "0": "";
                                String preceedeMinute = m < 10 ? "0" : "";
                                String amOrpm = h < 12 ? "am" :"pm";

                                binding.timeTxt.setText(prceedeHour+i + ":"+preceedeMinute + i1+" "+amOrpm);
                            } }, hour, minute, false);


                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        addScheduleViewModel = new  ViewModelProvider(this).get(AddScheduleViewModel.class);
        setListeners();



        View cv1, cv2, cv3, cv4, cv5, cv6;

        cv1 = binding.cv1;
        cv2 = binding.cv2;
        cv3 = binding.cv3;
        cv4 = binding.cv4;
        cv5 = binding.cv5;
        cv6 = binding.cv6;

        ArrayList<View> colorList = new ArrayList<>();

        colorList.add(cv1);
        colorList.add(cv2);
        colorList.add(cv3);
        colorList.add(cv4);
        colorList.add(cv5);
        colorList.add(cv6);

        for (View views : colorList) {
            views.setOnClickListener(this);
        }

    }

    private boolean ValidData(String scheduleName, String scheduleDateTime) {
        Log.d("--isValidData", "ValidData: "+scheduleName+" "+selectedDateAndTime);
       return  scheduleName != null && scheduleDateTime != null && !scheduleName.equals("") && !scheduleDateTime.equals("");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv1:
            colorValue = AppConstant.COLOR_PINK;
            binding.selectedColors.setVisibility(View.VISIBLE);
            binding.selectedColors.setBackgroundResource(R.color.selected_pink);
            break;

            case R.id.cv2:
           colorValue   = AppConstant.COLOR_ORANGE;
                binding.selectedColors.setVisibility(View.VISIBLE);
                binding.selectedColors.setBackgroundResource(R.color.selected_orange);
           break;


            case R.id.cv3:
                colorValue   = AppConstant.COLOR_YELLOW;
                binding.selectedColors.setVisibility(View.VISIBLE);
                binding.selectedColors.setBackgroundResource(R.color.selected_yellow);
                break;


            case R.id.cv4:
                colorValue   = AppConstant.COLOR_GREEN;
                binding.selectedColors.setVisibility(View.VISIBLE);
                binding.selectedColors.setBackgroundResource(R.color.selected_green);
                break;

            case R.id.cv5:
                colorValue   = AppConstant.COLOR_PURPLE;
                binding.selectedColors.setVisibility(View.VISIBLE);
                binding.selectedColors.setBackgroundResource(R.color.selected_purple);
                break;

            case R.id.cv6:
                colorValue   = AppConstant.COLOR_BLUE;
                binding.selectedColors.setVisibility(View.VISIBLE);
                binding.selectedColors.setBackgroundResource(R.color.color_accent);
                break;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddScheduleBinding.inflate(inflater,container,false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_add_schedule, container, false);
    }
}