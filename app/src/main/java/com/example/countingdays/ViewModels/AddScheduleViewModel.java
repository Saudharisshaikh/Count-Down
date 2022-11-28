package com.example.countingdays.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Repository.AppRepository;

public class AddScheduleViewModel extends BaseViewModel{

    private AppRepository appRepository;


    public AddScheduleViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application.getApplicationContext());
    }


    public void insertNewSchedule(Schedule schedule){


        if(schedule == null){
            return;
        }

        else{
            appRepository.insertSchedule(schedule);
            Log.d("--DataSaved", "insertNewSchedule: "+schedule.getScheduleName()+"  "+schedule.getDateTime());

        }
    }

}
