package com.example.countingdays.Listener;

import com.example.countingdays.Model.Schedule;

public interface DeleteCountDowns {

    void onCheckCountDown(Schedule schedule);
    void onUnCheckCountDown(Schedule schedule);
}
