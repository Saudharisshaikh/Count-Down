package com.example.countingdays.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.countingdays.Utils.AppConstant;
import com.example.countingdays.Utils.Utils;

@Entity(tableName = AppConstant.ENTITY_NAME)
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    int id;
    String scheduleName;
    String dateTime;

    public Schedule() {
    }

    public Schedule(String scheduleName, String dateTime) {
        this.scheduleName = scheduleName;
        this.dateTime = dateTime;
    }

    public Schedule(int id, String scheduleName, String dateTime) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
