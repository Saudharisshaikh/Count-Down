package com.example.countingdays.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Repository.AppRepository;

import java.util.List;

public class MainActivityViewModel extends BaseViewModel{

    public LiveData<List<Schedule>> getScheduleList;

    AppRepository appRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        appRepository = AppRepository.getInstance(application.getApplicationContext());
        getScheduleList = appRepository.getCompleteSchedules();
    }
}
