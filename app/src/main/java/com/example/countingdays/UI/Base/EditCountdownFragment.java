package com.example.countingdays.UI.Base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.countingdays.Adapters.AllScheduleAdapter;
import com.example.countingdays.Adapters.EditIndividualAdapter;
import com.example.countingdays.Listener.DeleteCountDowns;
import com.example.countingdays.Model.Schedule;
import com.example.countingdays.R;
import com.example.countingdays.Utils.Utils;
import com.example.countingdays.ViewModels.EditCountDownViewModel;
import com.example.countingdays.ViewModels.HomeViewModel;
import com.example.countingdays.databinding.FragmentEditCountdownBinding;
import com.google.android.gms.ads.AdRequest;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCountdownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCountdownFragment extends Fragment implements DeleteCountDowns {

    FragmentEditCountdownBinding binding;
    HomeViewModel homeViewModel;
    EditCountDownViewModel editCountDownViewModel;

    ArrayList<Schedule> selectedScheduleList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public EditCountdownFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCountdownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCountdownFragment newInstance(String param1, String param2) {
        EditCountdownFragment fragment = new EditCountdownFragment();
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
        binding = FragmentEditCountdownBinding.inflate(inflater,container,false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_edit_countdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.editCountdownRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.editCountdownRv.setFocusable(true);



        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getScheduleList.observe(getViewLifecycleOwner(),schedules -> {

        editCountDownViewModel = new ViewModelProvider(this).get(EditCountDownViewModel.class);


            if(schedules != null){

                EditIndividualAdapter editIndividualAdapter = new EditIndividualAdapter(getActivity(),schedules,this);
                binding.editCountdownRv.setAdapter(editIndividualAdapter);
            }
        });



        binding.deleteCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeFragment fragment = new HomeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        binding.deleteCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.showSuccessDialog(getActivity(),()->{

                    Schedule[] schedule = new Schedule[selectedScheduleList.size()];
//                    Schedule[] schedules1 = new Schedule[selectedScheduleList.size()];
//                    Log.d("--done", "onClick: Dialog Close"+schedules1[0].getScheduleName());
//                    selectedScheduleList.toArray(schedules1);

                    for(int i = 0; i<selectedScheduleList.size(); i++){

                        schedule[i] = selectedScheduleList.get(i);
                    }

                    Log.d("--getSchedulesList", "onClick: "+schedule[0].getScheduleName());
                    editCountDownViewModel.deleteSelectedSchedules(schedule);
                });

            }
        });
    }

    @Override
    public void onCheckCountDown(Schedule schedule) {

        selectedScheduleList.add(schedule);

    }

    @Override
    public void onUnCheckCountDown(Schedule schedule) {

     selectedScheduleList.remove(schedule);
    }
}