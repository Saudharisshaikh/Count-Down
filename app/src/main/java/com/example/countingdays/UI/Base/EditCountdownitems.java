package com.example.countingdays.UI.Base;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.countingdays.R;
import com.example.countingdays.databinding.FragmentEditCountdownitemsBinding;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCountdownitems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCountdownitems extends Fragment {

    FragmentEditCountdownitemsBinding binding;
    public int progressSchedule = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditCountdownitems() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCountdownitems.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCountdownitems newInstance(String param1, String param2) {
        EditCountdownitems fragment = new EditCountdownitems();
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
        binding = FragmentEditCountdownitemsBinding.inflate(inflater,container,false);

        return  binding.getRoot();
        //return inflater.inflate(R.layout.fragment_edit_countdownitems, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        int id = 0;
        String name = null;
        String dateTime = null;
        if(bundle != null){
             id = bundle.getInt("scheduleId");
             name = bundle.getString("scheduleName");
             dateTime = bundle.getString("scheduleDateTime");
            Log.d("--BundleHere", "onViewCreated: "+id+" "+dateTime+" "+name);
        }


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("--Back", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("--BackListener", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeFragment fragment = new HomeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });


        int finalId = id;
        String finalName = name;
        String finalDateTime = dateTime;
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putInt("scheduleId", finalId);
                bundle.putString("scheduleName", finalName);
                bundle.putString("scheduleDateTime", finalDateTime);

                EditIndividualFragment fragment = new EditIndividualFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("EditCountdownItem");
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });


        LocalDateTime time = LocalDateTime.parse(dateTime);

        long minutesRemain = ChronoUnit.MINUTES.between(LocalDateTime.now(),time);

        Log.d("--minutesRemain", "onViewCreated: "+minutesRemain);

        long monthcountdown = (int)(minutesRemain/43200);
        minutesRemain = (int)(minutesRemain%43200);
        long weekcountdown = (int)(minutesRemain/10080);
        minutesRemain = (int)(minutesRemain%10080);
        long daycountdown= (int)(minutesRemain/1440);
        minutesRemain = (int)(minutesRemain%1440);
        long hourcountdown = (int)(minutesRemain/60);
        minutesRemain = (int)(minutesRemain%60);

        String monthExtra = monthcountdown < 10?"0":"";
        String weekExtra = weekcountdown < 10?"0":"";
        String dayExtra   = daycountdown <10 ? "0":"";
        String hourExtra = hourcountdown < 10 ? "0":"";
        String minuteExtra = minutesRemain < 10 ? "0":"";

        Log.d("--monthValue", "onViewCreated: "+monthcountdown+" "+weekcountdown+" "+daycountdown+"  "+hourcountdown+"  "+minutesRemain);


        binding.monthDigit.setText(String.valueOf(monthExtra+monthcountdown));
        binding.weekDigit.setText(String.valueOf(weekExtra+weekcountdown));
        binding.dayDigit.setText(String.valueOf(dayExtra+daycountdown));
        binding.hourDigit.setText(String.valueOf(hourExtra+hourcountdown));
        binding.minuteDigit.setText(String.valueOf(minuteExtra+minutesRemain));


        binding.dueDate.setText("Due "+time.getDayOfMonth()+"-"+time.getMonthValue()+"-"+time.getYear());

        binding.ScheduleNameEdit.setText(name);

//        binding.progressBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                progressSchedule = progressSchedule+10;
//                binding.progressbar.setProgress(progressSchedule);
//            }
//        });
    }



}