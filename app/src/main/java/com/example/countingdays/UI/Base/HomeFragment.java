package com.example.countingdays.UI.Base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.countingdays.Adapters.AllScheduleAdapter;
import com.example.countingdays.Model.Schedule;
import com.example.countingdays.R;
import com.example.countingdays.ViewModels.HomeViewModel;
import com.example.countingdays.databinding.FragmentHomeBinding;
import com.google.android.ads.nativetemplates.rvadapter.AdmobNativeAdAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;
    HomeViewModel homeViewModel;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<Schedule> pastScheduleList = new ArrayList<>();
    List<Schedule> scheduleOrderList = new ArrayList<>();

    FloatingActionButton floatingActionButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //floatingActionButton = view.findViewById(R.id.fab);



        //binding.adView.setAdSize(AdSize.BANNER);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
        //binding.adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

         binding.homeScheduleRv.setLayoutManager(new LinearLayoutManager(getActivity()));
         binding.homeScheduleRv.setFocusable(true);

        homeViewModel = new  ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getScheduleList.observe(getViewLifecycleOwner(),
                schedules -> {

            //scheduleCorrectOrder(schedules);

                 if(schedules != null){

                     AllScheduleAdapter allScheduleAdapter = new AllScheduleAdapter(getActivity(),schedules);

                     AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                             requireContext().getResources().getString(R.string.native_id),
                             allScheduleAdapter,
                             "medium"
                     ).adItemInterval(2).build();

                     binding.homeScheduleRv.setAdapter(admobNativeAdAdapter);
                   if(schedules.size()>0){
                     binding.edit.setVisibility(View.VISIBLE);
                   }
                 }


                }

        );

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditCountdownFragment fragment = new EditCountdownFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });



        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddScheduleFragment fragment = new AddScheduleFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                //startActivity(new Intent(getActivity(),AddScheduleActivity.class));
            }
        });

    }

    private void scheduleCorrectOrder(List<Schedule> schedules) {

        for(Schedule schedule:schedules){

            LocalDateTime dateTime = LocalDateTime.parse(schedule.getDateTime());
            if(dateTime.isBefore(LocalDateTime.now())){

                pastScheduleList.add(schedule);

            }

        }
        List<Long> integers = new ArrayList<>();
        for(int i=0; i<schedules.size(); i++){



        }


        Log.d("--sortList", "scheduleCorrectOrder: "+integers);
    }
}