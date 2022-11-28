package com.example.countingdays.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.countingdays.Databases.MainDatabase;
import com.example.countingdays.Model.Schedule;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static  AppRepository ourInstance ;
    private MainDatabase database;


    private Executor executor = Executors.newSingleThreadExecutor();

    public AppRepository(Context context) {

        database = MainDatabase.getInstance(context);
    }

    public static AppRepository getInstance(Context context){
        return ourInstance =  new AppRepository(context);
    }

    // insert schedule
    public void insertSchedule(Schedule schedule){

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                database.schduleDAO().insertSchedule(schedule);
                Log.d("--databaseAdded", "run: called...");
            }
        });
    }

    // get all schedule List
    public LiveData<List<Schedule>> getCompleteSchedules(){

        return database.schduleDAO().getCompleteScheduleList();
    }

    public void updateSchedule(Schedule schedule){


        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                int update = database.schduleDAO().update(schedule);
                Log.d("--updateSuccessfully", "run: "+update);
            }
        });
    }

    public void deleteSchedule(Schedule schedule){

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                database.schduleDAO().delete(schedule);
            }
        });
    }

    public void deleteSelectedSchedules(Schedule ... schedules){

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                database.schduleDAO().deleteSchedules(schedules);
            }
        });
    }
}
