package com.example.countingdays.Databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.countingdays.Model.Schedule;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SchduleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSchedule(Schedule schedule);

    @Query("SELECT * FROM schedules")
    LiveData<List<Schedule>> getCompleteScheduleList();

    @Update
    int update(Schedule schedule);

    @Delete
    void delete(Schedule schedules);
//    @Query("UPDATE schedules SET scheduleName=:scheduleName,dateTime=:dateTime WHERE id = :id")
//    void update(String scheduleName,String dateTime, int id);

    @Delete
    void deleteSchedules(Schedule... schedules);
}
