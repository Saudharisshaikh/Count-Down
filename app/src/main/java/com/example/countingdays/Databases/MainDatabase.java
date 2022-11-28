package com.example.countingdays.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.Utils.AppConstant;

@Database(entities = {Schedule.class},version = 1)
public abstract class MainDatabase extends RoomDatabase{

    public static volatile MainDatabase  instance;
    public static final Object LOCK = new Object();
    public abstract SchduleDAO schduleDAO();

    public static MainDatabase getInstance(Context context){

        if(instance == null){

            synchronized (LOCK){
                if(instance == null){

                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MainDatabase.class, AppConstant.DATABASE_NAME
                            ).build();
                }
            }
        }
        return instance;
    }


}
