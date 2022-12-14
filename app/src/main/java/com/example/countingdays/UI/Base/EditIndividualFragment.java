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
import com.example.countingdays.ViewModels.EditIndividualViewModel;
import com.example.countingdays.databinding.FragmentEditIndividualBinding;
import com.google.android.gms.ads.AdRequest;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditIndividualFragment extends Fragment {

    String name = null;
    String dateTime = null;
    int id = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private final Calendar calendar = Calendar.getInstance();
    public int h = 0,m = 0,y = 0,mon = 0,d = 0 ;

    EditIndividualViewModel editIndividualViewModel;
    String selectedDateAndTime = null;

    FragmentEditIndividualBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditIndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditIndividualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditIndividualFragment newInstance(String param1, String param2) {
        EditIndividualFragment fragment = new EditIndividualFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditIndividualBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
        // return inflater.inflate(R.layout.fragment_edit_individual, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

         editIndividualViewModel = new ViewModelProvider(this).get(EditIndividualViewModel.class);




        Bundle bundle = this.getArguments();


        if(bundle != null){
            id = bundle.getInt("scheduleId");
            name = bundle.getString("scheduleName");
            dateTime = bundle.getString("scheduleDateTime");
            Log.d("--BundleHere", "onViewCreated: "+id+" "+dateTime+" "+name);
        }

        LocalDateTime time = changeIntoDate(dateTime);

        binding.title.setText(name);
        binding.dateTxt.setText(String.valueOf(time.getDayOfMonth()+"-"+time.getMonthValue()+"-"+time.getYear()));
        binding.timeTxt.setText(String.valueOf(time.getHour()+"-"+time.getMinute()));

        selectedDateAndTime = dateTime;
        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime changeIntoDate(String dateString){
        return   LocalDateTime.parse(dateString);
    }

    public void setListeners(){

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        binding.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.showSuccessDialog(getActivity(),()->{

                    Schedule schedule = new Schedule(id,name,dateTime);
                    editIndividualViewModel.deleteSchedule(schedule);
                    sendToHomeFragment();
                });

            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scheduleName = binding.title.getText().toString();
                String scheduleDateTime = selectedDateAndTime;

                Log.d("--dashBoard", "onClick: "+scheduleName);

                if(ValidData(scheduleName,scheduleDateTime)){

                    Schedule schedule = new Schedule(id,scheduleName,scheduleDateTime);
                    Log.d("--finalEdit", "onClick: "+schedule.getDateTime()+" "+schedule.getScheduleName());
                    editIndividualViewModel.updateSchedule(schedule);
                    sendToHomeFragment();
                }
                else{
                    Toast.makeText(getActivity(), "Please fill fields.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        binding.date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Calendar cl = Calendar.getInstance();

                LocalDateTime time = LocalDateTime.parse(dateTime);
                int yearValue = time.getYear();
                int monthValue = time.getMonthValue()-1;
                int dayValue = time.getDayOfMonth();

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
                }, yearValue,monthValue,dayValue);
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

                LocalDateTime time = LocalDateTime.parse(dateTime);
                int hourValue = time.getHour();
                int minuteValue = time.getMinute();

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.DateTimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                h = i;
                                m = i1;

                                if(mon != 12){
                                mon++;
                                }

                                Log.d("--Values", "onTimeSet: "+mon+" "+h+" "+m);


                                String sss = y+"-"+mon+"-"+d+" "+h+":"+m+":"+"00";

                                // convert to Date string
                                selectedDateAndTime = LocalDateTime.of(y, mon, d, h, m).toString();

                                // convert to string to Date
                                LocalDateTime time = LocalDateTime.parse(selectedDateAndTime);
                                Log.d("--getDates", "onTimeSet: "+time);

                                binding.timeTxt.setText(i + ":" + i1);
                            } }, hourValue, minuteValue, false);


                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });


    }

    public void sendToHomeFragment(){

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    private boolean ValidData(String scheduleName, String scheduleDateTime) {
        Log.d("--isValidData", "ValidData: "+scheduleName+" "+selectedDateAndTime);
        return !scheduleName.equals("") && !scheduleDateTime.equals("");
    }

}