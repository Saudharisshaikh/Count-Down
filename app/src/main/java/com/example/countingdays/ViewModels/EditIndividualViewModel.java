package com.example.countingdays.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Repository.AppRepository;

public class EditIndividualViewModel extends BaseViewModel{
    AppRepository repository;

    public EditIndividualViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void updateSchedule(Schedule schedule){

        repository.updateSchedule(schedule);
    }

    public void deleteSchedule(Schedule schedule){
        repository.deleteSchedule(schedule);
    }


}
