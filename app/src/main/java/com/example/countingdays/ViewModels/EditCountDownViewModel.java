package com.example.countingdays.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Repository.AppRepository;

public class EditCountDownViewModel extends BaseViewModel{
    AppRepository repository;

    public EditCountDownViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void deleteSelectedSchedules(Schedule ... schedules){

        repository.deleteSelectedSchedules(schedules);
    }
}
