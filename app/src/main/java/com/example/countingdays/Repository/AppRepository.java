package com.example.countingdays.Repository;

import static android.content.Context.ALARM_SERVICE;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.countingdays.Databases.MainDatabase;
import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Utils.AlarmBroadcastReceiver;
import com.example.countingdays.Utils.AppConstant;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static  AppRepository ourInstance ;
    private MainDatabase database;
    Context context;


    private Executor executor = Executors.newSingleThreadExecutor();

    public AppRepository(Context context) {

        this.context = context;
        database = MainDatabase.getInstance(context);
    }

    public static AppRepository getInstance(Context context){
        return ourInstance =  new AppRepository(context);
    }

    // insert schedule
    public void insertSchedule(Schedule schedule){

        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                database.schduleDAO().insertSchedule(schedule);
                Log.d("--databaseAdded", "run: called...");
                setAlarm(schedule);
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarm(Schedule schedules) {


            final int _id = (int) System.currentTimeMillis();
            LocalDateTime time = LocalDateTime.parse(schedules.getDateTime());
            Log.d("", "setAlarm: "+time.getMonthValue());

            Calendar calendar = Calendar.getInstance();
//
            Log.d("--times", "setAlarm: "+time.getHour()+"   "+ calendar.get(HOUR));

            calendar.set(time.getYear(), time.getMonthValue()-1, time.getDayOfMonth()-1);
            calendar.set(Calendar.HOUR, time.getHour()+12);
            calendar.set(Calendar.MINUTE, time.getMinute());
            calendar.set(SECOND,0);
            Log.d("--cla", "setAlarm: "+calendar.get(HOUR)+" "+time.getHour()+"  "+calendar.get(DATE)+"  "+calendar.get(MONTH));
            Log.d("--alarmSet", "setAlarm: "+calendar.get(MONTH)+" "+calendar.get(DATE)+"  "+calendar.get(HOUR)+"  "+calendar.get(MINUTE)+" "+calendar.getTimeInMillis());

// Alarm Manager Code


            Log.d("--bothTime", "setAlarm: System "+System.currentTimeMillis() + "   calender = "+calendar.getTimeInMillis()+ "  "+schedules.getScheduleName() );


           AlarmManager   alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.setAction("xyz.games.pacman.controller.BROADCAST");
            intent.putExtra("scheduleContent",schedules.getScheduleName()+"\n completed");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, schedules.getId(), intent,
                    PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent

            );
            Log.d("--afterNot", "setAlarm: "+calendar.get(MINUTE));




            LocalDateTime nowTime = LocalDateTime.now();
            if(time.getDayOfMonth() != nowTime.getDayOfMonth() ){
                Log.d("--abhiChala", "setAlarm: called");
                setAlarmBefore(schedules);
            }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarmBefore(Schedule schedules) {


        final int _id = (int) System.currentTimeMillis();
        LocalDateTime time = LocalDateTime.parse(schedules.getDateTime());
        Log.d("", "setAlarm: "+time.getMonthValue());

        Calendar calendar = Calendar.getInstance();
//
        Log.d("--times", "setAlarm: "+time.getHour()+"   "+ calendar.get(HOUR));

        calendar.set(time.getYear(), time.getMonthValue()-1, time.getDayOfMonth()-2);
        calendar.set(Calendar.HOUR, time.getHour()+12);
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(SECOND,0);
        Log.d("--cla", "setAlarm: "+calendar.get(HOUR)+" "+time.getHour()+"  "+calendar.get(DATE)+"  "+calendar.get(MONTH));
        Log.d("--alarmSet", "setAlarm: "+calendar.get(MONTH)+" "+calendar.get(DATE)+"  "+calendar.get(HOUR)+"  "+calendar.get(MINUTE)+" "+calendar.getTimeInMillis());

// Alarm Manager Code


        Log.d("--bothTime", "setAlarm: System "+System.currentTimeMillis() + "   calender = "+calendar.getTimeInMillis()+ "  "+schedules.getScheduleName() );


        AlarmManager   alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.setAction("xyz.games.pacman.controller.BROADCAST");
        intent.putExtra("scheduleContent",schedules.getScheduleName()+"\n1 day ago.");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id, intent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent

        );
        Log.d("--afterNot", "setAlarm: "+calendar.get(MINUTE));



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

                cancelSchedule(schedule);
                database.schduleDAO().delete(schedule);

            }
        });
    }

    private void cancelSchedule(Schedule schedule) {


        AlarmManager   alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, schedule.getId(), intent,
                PendingIntent.FLAG_ONE_SHOT);

        alarmManager.cancel(pendingIntent);
        Log.d("--cancelSchedule", "cancelSchedule: ");
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
